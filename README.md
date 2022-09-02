# DCM Glitch calculator

Program to calculate the glitches from a Double Crystal Monochromator given a crystal orientation and show them in "spaghetti"-type plots and glitch patterns (showing most common absoption edges positions)

Si111 and Si220 crystals are implemented but it is easy to add more.

The calculations are based on the following publication:

DETERMINATION OF GLITCHES IN SOFT X-RAY MONOCHROMATOR CRYSTALS
by Van der Laan and Thole, 
Nuclear Instruments and Methods in Physics Research, A263 (1988) 515-521.

### Dependencies

It uses the following libraries from the same author:
 - com.vava33.BasicPlotPanel
 - com.vava33.cellsymm
 - com.vava33.vavaUtils

And the following 3rd party libraries:
 - net.miginfocom.swing.MigLayout (MigLayout http://www.miglayout.com/)
 - org.apache.commons.math3.util.FastMath (Apache Commons Math https://commons.apache.org/proper/commons-math)

## Authors

  - **Oriol Vallcorba**

## Acknowledgemends

To NOTOS staff (in 2019) Giovanni Agostini and Dominique Heinis.

## License

This project is licensed under the [GPL-3.0 license](LICENSE.txt)
