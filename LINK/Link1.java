// Zelda . java Copyright (C) 2020 Ben Sanders
// TODO: Add weapons and enemy li v e s .
import java . u t i l . Vector ;
import java . u t i l .Random;
import java . time . LocalTime ;
import java . time . temporal . ChronoUnit ;
import javax . swing . JFrame ;
import javax . swing . JPanel ;
import javax . swing . JButton ;
import javax . swing .JComponent ;
import javax . swing . KeyStroke ;
import javax . swing . AbstractAct ion ;
import javax . swing .JComboBox ;
import javax . imageio . ImageIO ;
import java . awt . image . BufferedImage ;
import java . i o . F i l e ;
import java . i o . IOException ;
import java . awt . event . ActionEvent ;
import java . awt . event . Act ionL istener ;
import java . awt . Graphics ;
import java . awt . Graphics2D ;
import java . awt . geom . AffineTransform ;
import java . awt . image . AffineTransformOp ;
import javax . sound . sampled . AudioInputStream ;

import javax . sound . sampled . AudioSystem ;
import javax . sound . sampled . C l ip ;
public class Zelda
{
public Zelda ( )
{
setup ( ) ;
}
public stat ic void setup ( )
{
appFrame = new JFrame ( ”The Legend o f Zelda : Link ’ s Awakening ” ) ;
XOFFSET = 0 ;
YOFFSET = 40;
WINWIDTH = 338;
WINHEIGHT = 271;
p i = 3.14159265358979;
quarterP i = 0.25 ∗ p i ;
h a l fP i = 0.5 ∗ p i ;
threequartersP i = 0.75 ∗ p i ;
f i v equa r t e r sP i = 1.25 ∗ p i ;
threeha lvesP i = 1.5 ∗ p i ;
sevenquartersP i = 1.75 ∗ p i ;
twoPi = 2.0 ∗ p i ;
endgame = fa lse ;
p1width = 20; //18 .5 ;
p1height = 20; //25;
p1or ig ina lX = ( double )XOFFSET + ( ( double )WINWIDTH / 2 .0 ) − ( p1width /
2 .0 ) ;
p1or ig ina lY = ( double )YOFFSET + ( ( double )WINHEIGHT / 2 .0 ) − ( p1height
/ 2 .0 ) ;
l e v e l = 3 ;
aud io l i f e t im e = new Long(78000) ; // 78 seconds f o r KI .wav
dropL i feL i fe t ime = new Long (1000 ) ; // 1 second
try
{
// s e t tin g up the Koholin t Island images
xdimKI = 16;
ydimKI = 16;
backgroundKI = new Vector< Vector< BufferedImage > >( ) ;
for ( int i = 0 ; i < ydimKI ; i ++ )
{
Vector< BufferedImage > temp = new Vector< BufferedImage >( ) ;
for ( int j = 0 ; j < xdimKI ; j ++ )
{
    BufferedImage tempImg = ImageIO . read ( new F i l e ( ” blank . png”
    ) ) ;
    temp . addElement ( tempImg ) ;
    }
    backgroundKI . addElement ( temp ) ;
    }
    for ( int i = 0 ; i < backgroundKI . s iz e ( ) ; i ++ )
    {
    for ( int j = 0 ; j < backgroundKI . elementAt ( i ) . s iz e ( ) ; j ++ )
    {
    i f ( ( j == 5 && i == 10 ) | | ( j == 5 && i == 11 ) | | ( j ==
    6 && i == 10 ) | | ( j == 6 && i == 11 ) | |
    ( j == 7 && i == 10 ) | | ( j == 7 && i == 11 ) | | ( j ==
    8 && i == 9 ) | | ( j == 8 && i == 10 ) ) // TODO swap
    j and i
    {
    Str ing filename = ” KI ” ;
    i f ( j < 10 )
    {
    filename = filename + ”0” ;
    }
    filename = filename + j ;
    i f ( i < 10 )
    {
    filename = filename + ”0” ;
    }
    filename = filename + i + ” . png” ;
    //System . out . p ri n tl n ( filename ) ;
    backgroundKI . elementAt ( i ) . se t ( j , ImageIO . read ( new F i l e (
    filename ) ) ) ;
    }
    }
    }
    // s e t tin g up the Koholin t Island walls
    wa l lsKI = new Vector< Vector< Vector< ImageObject > > >( ) ;
    for ( int i = 0 ; i < ydimKI ; i ++ )
    {
    Vector< Vector< ImageObject > > temp = new Vector< Vector<
    ImageObject > >( ) ;
    for ( int j = 0 ; j < xdimKI ; j ++ )
    {
    Vector< ImageObject > tempWalls = new Vector< ImageObject
    >( ) ;
    temp . addElement ( tempWalls ) ;
    }
    wa l lsKI . add ( temp ) ;
}
for ( int i = 0 ; i < wa l lsKI . s iz e ( ) ; i ++ )
{
for ( int j = 0 ; j < wa l lsKI . elementAt ( i ) . s iz e ( ) ; j ++ )
{
i f ( i == 5 && j == 10 )
{
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 270, 35 , 68 , 70 , 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 100, 100, 200, 35 , 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 100, 135, 35 , 35 , 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 0 , 165, 35 , 135, 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 100, 200, 35 , 100, 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 135, 270, 200, 35 , 0.0 ) ) ;
}
i f ( i == 8 && j == 9 )
{
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 0 , 35 , 135, 35 , 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 100, 70 , 35 , 140, 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 35 , 135, 35 , 100, 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 0 , 170, 35 , 70 , 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 0 , 235, 35 , 70 , 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 0 , 270, 135, 35 , 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 170, 270, 135, 35 , 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 300, 35 , 35 , 270, 0.0 ) ) ;
wa l lsKI . elementAt ( i ) . elementAt ( j ) . addElement ( new
ImageObject ( 235, 35 , 70 , 35 , 0.0 ) ) ;
}
}
}
// s e t tin g up the T ail Cave images
