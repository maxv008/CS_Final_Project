package com.company;

import java.awt.*;

/**
 * Created by daniel on 6/5/16.
 */
public class Helper {
    public static Rectangle getCenteredBounds( Dimension size ) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Point location = new Point( ( screen.width - size.width ) / 2,
                ( screen.height - size.height ) / 2 );
        return new Rectangle( location, size );
    }

    public static Rectangle getCenteredBounds( int width, int height ) {
        return getCenteredBounds( new Dimension( width, height ) );
    }
}
