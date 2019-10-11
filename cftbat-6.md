# 6 - Organizing your project: a librarian's tale

Summary: `def`, `ns`, `refer`, `alias`, `require`, `use`

## Your project as a library

* *namespaces* map human friendly names and objects
* Every object lives in a namespace
* `(ns-name my.namespace)` gives the name of a namespace
* `*ns*` refers to the current namespace

## Storing objects with `def`

* `def` *interns* variables, it maps a symbol to an object
* `(ns-interns *ns*)` returns a map of symbols to objects for the namespace
* `(ns-map *ns*) returns the full map used by Clojure for a namepsace
* `#'user/my-var` is the *reader form* of a var
* `(deref #'user/my-var)` gets the objects the symbol points to


## Creating and switching to namespaces

* three tools for creating namespaces
  1. `create-ns` - create a namespace (don't move into it, not really useful)
  2. `in-ns` - create a namespace and move into it
  3. `ns` macro
* fully qualified name is `my-ns/my-var`

### `refer`

* `refer` merges symbols from another namespace into the current namespace
* `:only`, `:exclude`, and `:rename` allow limiting and renaming mapped symbols
* `:rename` takes a map of symbol to renamed symbol
* `(clojure.core/refer-clojure) maps `clojure.core` into current namespace
* `(defn= my-private-fn [])` creates a namespace-private function

### `alias`

* allows you to shorten the name of a namespace for fully-qualified symbols
* `(alias 'short-ns 'long-namespace-name)` 

## Real project organization

### Relationship between file paths and namespace names

When creating apps with lein
  1. source code's root is `src`
  2. dashes in namespace name become underscores in file system path
  3. component preceding a dot (`.`) is a file system directory
  4. final component (`clj` extension) is a source file
   
### Requiring and using namespaces

* `(require 'the-divine-cheese-code.visualization.svg)` finds the Clojure source
  and compiles it
* `(refer 'the-divine-cheese-code.visualization.svg)`maps symobols in the svg
  namespace to the current namespace
* `(require '[the-divine-cheese-code.visualization.svg :as svg])` is equivalent
  to using `alias` after `require`
* `(use 'the-divine-cheese-code.visualization.svg)` is equivalent to `require`
  then `refer`
* `:as` can also be used with `use` to alias namespaces

### The `ns` macro

* Can incorporate `require`, `use`, `in-ns`, `alias`, `refer` as references
* `:refer-clojure` lets you control what is imported from clojure.core
* `ns` macro automatically refers `clojure.core`
* `:require`
```
(ns the-divine-cheese-code.core
  (:require [the-divine-cheese-code.visualization.svg :as svg]
            [clojure.java.browse :as browse]))
```
is equivalent to
```
(in-ns 'the-divine-cheese-code.core)
(require ['the-divine-cheese-code.visualization.svg :as 'svg])
(require ['clojure.java.browse :as browse])
```
* `:require` allows referring single names from a namespace or `:all`
* `:use` is not usually seen
```
(ns the-divine-cheese-code.core
  (:use [clojure.java browse io]))
```
is equivalent to
```
(in-ns 'the-divine-cheese-code.core)
(use 'clojure.java.browse)
(use 'clojure.java.io)
```
