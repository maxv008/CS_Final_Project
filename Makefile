
OFILES=mpfit.o
LIBFILE=libmpfit.a

all: $(LIBFILE) Plagdata

clean:
	rm -f $(OFILES) Plagdata $(LIBFILE)

mpfit.o: mpfit.c mpfit.h
	$(CC) -c -o $@ $< $(CFLAGS)

$(LIBFILE): $(OFILES)
	$(AR) r $@ $(OFILES)

testmpfit: Plagdata.c libmpfit.a
	$(CC) -o $@ $(CFLAGS) Plagdata.c -L. -lmpfit -lm
