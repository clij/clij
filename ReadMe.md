# CLIJ: GPU-accelerated image processing in Fiji
## Introduction
CLIJ is an OpenCL - ImageJ bridge and a [Fiji](https://fiji.sc/) plugin allowing users with entry-level skills in programming 
to build GPU-accelerated workflows to speed up their image processing. Increased efforts were put on documentation, code examples, interoperability, and extensibility.
CLIJ is based on 
[ClearCL](http://github.com/ClearControl/ClearCL), 
[Imglib2](https://github.com/imglib), 
[ImageJ](http://imagej.net) and 
[SciJava](https://github.com/SciJava).

**If you use CLIJ, please cite it:**

Robert Haase, Loic Alain Royer, Peter Steinbach, Deborah Schmidt, 
Alexandr Dibrov, Uwe Schmidt, Martin Weigert, Nicola Maghelli, Pavel Tomancak, 
Florian Jug, Eugene W Myers. 
*CLIJ: Enabling GPU-accelerated image processing in Fiji*. BioRxiv preprint. [https://doi.org/10.1101/660704](https://doi.org/10.1101/660704)

If you search for support, please open a thread on the [image.sc](https://image.sc) forum.

[![Image.sc forum](https://img.shields.io/badge/dynamic/json.svg?label=forum&url=https%3A%2F%2Fforum.image.sc%2Ftags%2Fclij.json&query=%24.topic_list.tags.0.topic_count&colorB=brightgreen&suffix=%20topics&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAABPklEQVR42m3SyyqFURTA8Y2BER0TDyExZ+aSPIKUlPIITFzKeQWXwhBlQrmFgUzMMFLKZeguBu5y+//17dP3nc5vuPdee6299gohUYYaDGOyyACq4JmQVoFujOMR77hNfOAGM+hBOQqB9TjHD36xhAa04RCuuXeKOvwHVWIKL9jCK2bRiV284QgL8MwEjAneeo9VNOEaBhzALGtoRy02cIcWhE34jj5YxgW+E5Z4iTPkMYpPLCNY3hdOYEfNbKYdmNngZ1jyEzw7h7AIb3fRTQ95OAZ6yQpGYHMMtOTgouktYwxuXsHgWLLl+4x++Kx1FJrjLTagA77bTPvYgw1rRqY56e+w7GNYsqX6JfPwi7aR+Y5SA+BXtKIRfkfJAYgj14tpOF6+I46c4/cAM3UhM3JxyKsxiOIhH0IO6SH/A1Kb1WBeUjbkAAAAAElFTkSuQmCC)](https://forum.image.sc/tags/clij)

<img src="./images/clij_bridge.gif">

## Overview

* [CLIJ - a quick tour](https://clij.github.io/clij-docs/quickTour)
* Installation
  * [Fiji update site](https://clij.github.io/clij-docs/installationInFiji)
  * [MicroManager 2.0](https://clij.github.io/clij-docs/installationInMicroManager)
  * [Depending on CLIJ via maven](https://clij.github.io/clij-docs/dependingViaMaven)
* Introduction to CLIJ programming
  * [CLIJ for ImageJ Macro programmers](https://clij.github.io/clij-docs/macro_intro)
  * [CLIJ for Java programmers](https://clij.github.io/clij-docs/api_intro)
  * [ImageJ Jupyter notebooks in Groovy](https://github.com/clij/clij-notebooks/blob/master/clij-intro.ipynb)
  * [Execution from the command line](https://github.com/clij/clij-executable-example)
  * [CLIJ ImageJ Ops in Java](https://clij.github.io/clij-docs/clij_imagej_ops_java)
  * [CLIJ ImageJ Ops in the Script Editor](https://clij.github.io/clij-docs/clij_imagej_ops_scripteditor)
  * [Release notes](https://github.com/clij/clij/releases)
  * [Release cycle](https://clij.github.io/clij-docs/release_cycle)
* Application programming interface (API)
  * [API design principles](https://clij.github.io/clij-docs/api_design_priciples)
  * [ImageJ Macro](https://clij.github.io/clij-docs/reference)
  * [Jython](https://clij.github.io/clij-docs/referenceJython)
  * [Java](https://clij.github.io/clij-docs/referenceJava)
  * [Groovy](https://clij.github.io/clij-docs/referenceGroovy)
* Code examples
  * [ImageJ Macro](https://github.com/clij/clij-docs/tree/master/src/main/macro)
  * [BeanShell](https://github.com/clij/clij-docs/tree/master/src/main/beanshell)
  * [Jython](https://github.com/clij/clij-docs/tree/master/src/main/jython)
  * [JavaScript](https://github.com/clij/clij-docs/tree/master/src/main/javascript)
  * [Groovy](https://github.com/clij/clij-docs/tree/master/src/main/groovy)
  * [Java](https://github.com/clij/clij-docs/tree/master/src/main/java/net/haesleinhuepf/clij/examples)
  * [ImageJ Ops in Java](https://github.com/clij/clij-ops/tree/master/src/test/java/net/haesleinhuepf/clij/ops/examples)
  * [ImageJ Ops in Jython](https://github.com/clij/clij-ops/tree/master/src/test/resources/jython)
* Benchmarking
  * [Benchmarking CLIJ operations versus ImageJ/Fiji operations](https://clij.github.io/clij-benchmarking/benchmarking_operations)
  * [Benchmarking a CLIJ workflow versus ImageJ/Fiji](https://clij.github.io/clij-benchmarking/benchmarking_workflow_spot_count)
* Extending CLIJ functionality
  * [Plugin template](https://github.com/clij/clij-plugin-template/)
  * [Example plugin for convolution/deconvolution](https://github.com/clij/clij-custom-convolution-plugin/)
* Troubleshooting
  * [List of tested systems](https://clij.github.io/clij-docs/testedsystems)
  * [Troubleshooting](https://clij.github.io/clij-docs/troubleshooting)
  * [Support](https://image.sc)

## Acknowledgements
Development of CLIJ is a community effort. We would like to thank everybody who helped developing and testing. In particular thanks goes to
Bruno C. Vellutini (MPI CBG),
Curtis Rueden (UW-Madison LOCI),
Damir Krunic (DKFZ),
Daniel J. White (GE),
Gaby G. Martins (IGC),
Si&acirc;n Culley (LMCB MRC),
Giovanni Cardone (MPI Biochem),
Jan Brocher (Biovoxxel), 
Johannes Girstmair (MPI CBG),
Juergen Gluch (Fraunhofer IKTS),
Kota Miura (Heidelberg),
Laurent Thomas (Acquifer), 
Nico Stuurman (UCSF),
Pavel Tomancak (MPI CBG),
Pradeep Rajasekhar (Monash University),
Tobias Pietzsch (MPI-CBG),
Wilsom Adams (VU Biophotonics)


  
