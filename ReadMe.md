# CLIJ: GPU-accelerated image processing for everyone
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
*CLIJ: GPU-accelerated image processing for everyone*. BioRxiv preprint. [https://doi.org/10.1101/660704](https://doi.org/10.1101/660704)

If you search for support, please open a thread on the [image.sc](https://image.sc) forum.

[![Image.sc forum](https://img.shields.io/badge/dynamic/json.svg?label=forum&url=https%3A%2F%2Fforum.image.sc%2Ftags%2Fclij.json&query=%24.topic_list.tags.0.topic_count&colorB=brightgreen&suffix=%20topics&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAABPklEQVR42m3SyyqFURTA8Y2BER0TDyExZ+aSPIKUlPIITFzKeQWXwhBlQrmFgUzMMFLKZeguBu5y+//17dP3nc5vuPdee6299gohUYYaDGOyyACq4JmQVoFujOMR77hNfOAGM+hBOQqB9TjHD36xhAa04RCuuXeKOvwHVWIKL9jCK2bRiV284QgL8MwEjAneeo9VNOEaBhzALGtoRy02cIcWhE34jj5YxgW+E5Z4iTPkMYpPLCNY3hdOYEfNbKYdmNngZ1jyEzw7h7AIb3fRTQ95OAZ6yQpGYHMMtOTgouktYwxuXsHgWLLl+4x++Kx1FJrjLTagA77bTPvYgw1rRqY56e+w7GNYsqX6JfPwi7aR+Y5SA+BXtKIRfkfJAYgj14tpOF6+I46c4/cAM3UhM3JxyKsxiOIhH0IO6SH/A1Kb1WBeUjbkAAAAAElFTkSuQmCC)](https://forum.image.sc/tags/clij)

<img src="./images/clij_bridge.gif">

## Overview

* [CLIJ - a quick tour](https://clij.github.io/clij-docs/quickTour)
* Installation
  * [Fiji update site](https://clij.github.io/clij-docs/installationInFiji)
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
  * [ImageJ Macro](https://clij.github.io/clij-docs/reference)
  * [ImageJ Macro cheat sheet](https://github.com/clij/clij-docs/blob/master/clij_cheatsheet.pdf)
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
  * [Benchmarking CLIJ operations versus ImageJ/Fiji operations using JMH](https://clij.github.io/clij-benchmarking/benchmarking_operations_jmh)
  * [Benchmarking CLIJ operations versus ImageJ/Fiji operations (archived)](https://clij.github.io/clij-benchmarking/benchmarking_operations)
  * [Benchmarking a CLIJ workflow versus ImageJ/Fiji](https://clij.github.io/clij-benchmarking/benchmarking_workflow_spot_count)
* Extending CLIJ functionality
  * [Plugin template](https://github.com/clij/clij-plugin-template/)
  * [Example plugin for convolution/deconvolution](https://github.com/clij/clij-custom-convolution-plugin/)
* CLIJx - the upcoming successor of CLIJ
  * [CLIJ2 development repository](https://github.com/clij/clij-advanced-filters) (experimental)
  * [Icy](https://clij.github.io/clicy/) (experimental)
  * [ImageJ1](https://github.com/clij/clij-legacy/) (experimental)
  * [Matlab](https://clij.github.io/clatlab/) (experimental)
  * [MicroManager 2.0](https://clij.github.io/clij-docs/installationInMicroManager) (experimental)
  * [Python](https://clij.github.io/clijpy/) (experimental)  
* FAQ / support
  * [Frequently asked questions](https://clij.github.io/clij-docs/faq)
  * [List of tested systems](https://clij.github.io/clij-docs/testedsystems)
  * [Troubleshooting](https://clij.github.io/clij-docs/troubleshooting)
  * [Support](https://image.sc)
  * [Imprint](https://clij.github.io/imprint)

## Acknowledgements
Development of CLIJ is a community effort. We would like to thank everybody who helped developing and testing. In particular thanks goes to
Alex Herbert (University of Sussex),
Bram van den Broek (Netherlands Cancer Institute),
Brenton Cavanagh (RCSI),
Brian Northan (True North Intelligent Algorithms),
Bruno C. Vellutini (MPI CBG),
Curtis Rueden (UW-Madison LOCI),
Damir Krunic (DKFZ),
Daniel J. White (GE),
Gaby G. Martins (IGC),
Guillaume Witz (Bern University),
Si&acirc;n Culley (LMCB MRC),
Giovanni Cardone (MPI Biochem),
Jan Brocher (Biovoxxel), 
Jean-Yves Tinevez (Institute Pasteur),
Johannes Girstmair (MPI CBG),
Juergen Gluch (Fraunhofer IKTS),
Kota Miura,
Laurent Thomas (Acquifer), 
Matthew Foley (University of Sydney),
Nico Stuurman (UCSF),
Peter Haub,
Pete Bankhead (University of Edinburgh),
Pradeep Rajasekhar (Monash University),
Tanner Fadero (UNC-Chapel Hill),
Thomas Irmer (Zeiss),
Tobias Pietzsch (MPI-CBG),
Wilson Adams (VU Biophotonics)

R.H. was supported by the German Federal Ministry of
Research and Education (BMBF) under the code 031L0044
(Sysbio II) and D.S. received support from the German
Research Foundation (DFG) under the code JU3110/1-1.
P.T. was supported by the European Regional
Development Fund in the IT4Innovations national
supercomputing center-path to exascale project,
project number CZ.02.1.01/0.0/0.0/16_013/0001791
within the Operational Programme Research, Development
and Education.

[Imprint](https://clij.github.io/imprint)
  
