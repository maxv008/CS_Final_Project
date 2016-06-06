#include "mpfit.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#pragma warning( disable : 4996)

/* This is the private data structure which contains the data points
   and their uncertainties */
struct vars_struct {
	double *x;
	double *y;
};

/* Simple routine to print the fit results */
void printresult(double *x, double *xact, mp_result *result)
{
	int i;

	if ((x == 0) || (result == 0)) return;
	printf("  CHI-SQUARE = %f    (%d DOF)\n",
		result->bestnorm, result->nfunc - result->nfree);
	printf("        NPAR = %d\n", result->npar);
	printf("       NFREE = %d\n", result->nfree);
	printf("     NPEGGED = %d\n", result->npegged);
	printf("     NITER = %d\n", result->niter);
	printf("      NFEV = %d\n", result->nfev);
	printf("\n");
	if (xact) {
		for (i = 0; i < result->npar; i++) {
			printf("  P[%d] = %f +/- %f     (ACTUAL %f)\n",
				i, x[i], result->xerror[i], xact[i]);
		}
	}
	else {
		for (i = 0; i < result->npar; i++) {
			printf("  P[%d] = %f +/- %f\n",
				i, x[i], result->xerror[i]);
		}
	}

}

int statfunc(int m, int n, double *p, double *dy, double **dvec, void *vars)
{
	int i, j;
	struct vars_struct *v = (struct vars_struct *) vars;
	double *x, *y, f;

	x = v->x;
	y = v->y;

	for (i = 0; i < m; i++) {
		f = 0;
		for (j = 0; j < 2; j++) { //(sizeof(p)/sizeof(p[0]) should be the same as n.
			f += p[2 * j] * (2.0 / (1.0 + exp(-p[2 * j + 1] * (&x[i])[j] * (&x[i])[j])) - 1.0);
		}
		dy[i] = (y[i] - f);
	}

	return 0;
}

/* Test harness routine, which contains test data, invokes mpfit() */
int testfit()
{
	int j, k = 0;
	FILE *data;
	FILE *output;
	data = fopen("Compiled_Data.txt", "r");
	output = fopen("Constants.ini", "w");
	int n;
	fscanf(data, "%d", &n);
	double p[4];
	double x[820][2];
	double y[820];

	for (j = 0; j < n; j++)
	{
		fscanf(data, "%lf", &p[j]);
	}
	while (fscanf(data, "%lf,%lf,%lf", &x[k][0], &x[k][1], &y[k]) != EOF)
	{
		k++;
	}

	int i;
	struct vars_struct v;
	int status;
	mp_result result;
	mp_par constraints[4];

	memset(&result, 0, sizeof(result));       /* Zero results structure and parameter constraints */
	memset(constraints, 0, sizeof(constraints));
	for (i = 0; i < n / 2; i++)
	{
		constraints[2 * i].limited[0] = 1;
		constraints[2 * i].limited[1] = 1;
		constraints[2 * i].limits[0] = 0.0;
		constraints[2 * i].limits[1] = 100.0;
	}

	v.x = x;
	v.y = y;

	/* Call fitting function for 820 data points and 4 (n) parameters */
	status = mpfit(statfunc, 820, n, p, constraints, 0, (void *)&v, &result);

	fprintf(output, "*** testfit status = %d (Editing this file is not recommended)\n", status);
	for(i = 0; i < n; i++)
	{
		if (i % 2 == 0)
		{
			fprintf(output, "Maximum %d = %lf\n", i / 2, p[i]);
		} else
		{
			fprintf(output, "Weight %d = %lf\n", i / 2, p[i]);
		}
	}
	//printresult(p, &result);

	return 0;
}


int main(int argc, char **argv[])
{
	testfit();
	exit(0);
}
