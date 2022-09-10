# DCM Glitches calculator

Program to calculate the glitches from a Double Crystal Monochromator given a crystal orientation and show them as "spaghetti"-type plots and glitch patterns (showing most common absoption edges positions).

Si111 and Si220 crystals are implemented but more can be added easily.

The calculations are based on the following publication:

[DETERMINATION OF GLITCHES IN SOFT X-RAY MONOCHROMATOR CRYSTALS by G. Van der Laan and B.T. Thole, *Nuclear Instruments and Methods in Physics Research*, A263 (1988) 515-521](https://www.sciencedirect.com/science/article/abs/pii/0168900288909953)

### Dependencies

DCM Glitches calculator is completely programmed with JavaTM (www.java.com) using OpenJDK version 11.0.9.1 (GNU General Public License, version 2, with the Classpath Exception: https://openjdk.java.net/legal/gplv2+ce.html). You may find Oracle's free, GPL-licensed, production-ready OpenJDK binaries necessary to run it at https://openjdk.java.net/.

The program uses the [com.vava33.BasicPlotPanel](https://github.com/ovallcorba/BasicPlotPanel) plotting library from the same author. It also uses the [vavaUtils libraries](https://github.com/ovallcorba/vavaUtils) com.vava33.cellsymm and com.vava33.jutils

The following 3rd party libraries are also used:
- net.miginfocom.swing.MigLayout (MigLayout. http://www.miglayout.com)
  BSD license: http://directory.fsf.org/wiki/License:BSD_4Clause
- org.apache.commons.math3.util.FastMath (Apache Commons Math. https://commons.apache.org/proper/commons-math/)
  Apache License: http://www.apache.org/licenses/LICENSE-2.0

(No changes on the source codes of these libraries have been made, you can download the source codes for these libraries at their respective websites).

### Usage

Binaries for windows and linux can be downloaded in the releases section (https://github.com/ovallcorba/DCM_Glitch_calculator/releases). There may be also executable jar files in the dist folder. Otherwise you need to clone the project, gather the dependencies and build/run it.

## Authors

  - **Oriol Vallcorba**

## Disclaimer

This software is distributed WITHOUT ANY WARRANTY. The authors (or their institutions) have no liabilities in respect of errors in the software, in the documentation and in any consequence of erroneous results or damages arising out of the use or inability to use this software. Use it at your own risk.

## Acknowledgments 

Thanks are due the Spanish "Ministerio de Ciencia e Innovación", to the "Generalitat the Catalunya" and to the ALBA Synchrotron for continued financial support.

To NOTOS staff (in 2019) Giovanni Agostini and Dominique Heinis.

## License

This project is licensed under the [GPL-3.0 license](LICENSE.txt)

Citation of the author/program/affiliation would be greatly appreciated when this program helped to your work.