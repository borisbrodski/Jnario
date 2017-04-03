[![Build Status](https://travis-ci.org/borisbrodski/Jnario.svg?branch=no_xtend_xtext2.9)](https://travis-ci.org/borisbrodski/Jnario)

##Overview

Jnario is a framework for writing executable specifications for Java. It provides all the goodness of Xtend while being specifically tailored to writing acceptance and unit tests. It is easy to integrate as Jnario specifications are compiled to plain Java Junit tests. Head over to [Jnario.org](http://www.jnario.org) for more information.

See the [release notes](release_notes.md) for the current version.

#Dependencies & Installation

* Jnario requires at least [Eclipse 3.6](http://eclipse.org/downloads/) with [Xtext 2.6](http://www.xtext.org) and [Xtend 2.6](http://www.eclipse.org/xtend/). You can install both from this update site `http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/`.
* The latest snapshot verion of Jnario can be installed from the following update site `http://www.jnario.org/updates/snapshot/`.

##Examples

###Acceptance Specification

```cucumber
Feature: Addition

  In order to avoid silly mistakes
  As a math idiot
  I want to be told the sum of two numbers
  
  Scenario: Add two numbers
    Calculator calculator = new Calculator()
    Given I have entered "50" into the calculator
      calculator.enter(args.first)
    And I have entered "70" into the calculator
    When I press "add"
      calculator.press(args.first)
    Then the result should be "120" on the screen
      calculator.result => args.first
```

###Unit Specifications

```java
package example

import java.util.Stack
import java.util.EmptyStackException

describe Stack{
  context "empty stack"{
    fact subject.size should be 0
    fact subject.pop throws EmptyStackException
  }
  context "one element"{ 
    before subject.add("element")
    fact subject.size should be 1
    fact "pop decreases size"{
      subject.pop
      subject.size => 0 
    }
    fact "pop removes last element"{
      subject.pop => "element"
      subject.contains("element") => true
    } 
  } 
}
```

##Setting up a development environment

* If you haven't got an Eclipse installation, download the [Xtend Distribution](http://www.eclipse.org/xtend/download.html). 
* Install the latest Xtext 2.6.1 release from: `http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/` (this is not required if you have downloaded the Xtend distribution).
* Install the latest Jnario snapshot: `http://www.jnario.org/updates/snapshot/`.
* Clone the jnario repository: 

``` 
$ git clone git://github.com/bmwcarit/Jnario.git
``` 

* Start Eclipse and import all projects from jnario/* into your Eclipse workspace
* Open the target definition **tools/org.jnario.target/juno.target** and click **Set as Target Plattform** in the upper right corner
* If you want to change the grammar import **org.eclipse.xtend.core** and **org.eclipse.xtext.xbase** into your workspace via **import->Plug-in Development->Plug-ins and Fragments** with **Import As->Projects with source folder**. This is necessary for the workflow to find the custom source classes of Xtend and Xbase.

##Build

Build with Maven 3 and Tycho. Run the following command in the repository root:

``` 
mvn clean install
``` 
