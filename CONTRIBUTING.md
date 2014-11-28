Contributing to StormCloud
==========================

PR submission guidelines
------------------------

First, check if an issue is raised for your particular problem/feature - if not, raise one.
Second, check out the code and make your changes.
Next, push your changes to your fork of the project in a feature branch
Finally, open a pull request with your changes and wait for a reviewer to have a look. If there are any problems, we'll let you know, and you can update your feature branch.
All contributions are welcomed and much appreciated.

Code style preferences
----------------------

Generally, follow Sun/Oracle standards for Java.

We use:

* Four-space tabs:

```
public String getName() {
    return this.name;
}
```

* Curly braces with one space, on the same line as the statement:

```
if (height < MIN_HEIGHT) {
    //..
}

while (isTrue) {
    //..
}

switch (foo) {
    //..
}
```

* Constant names in all-caps (with underscore):

```
final static String FOO_BAR = "baz";

static final String FOO_BAR = "baz";
```

* Conditionals with one space:

```
if (true) {
    //...
}

while (true) {
    //...
}

switch (v) {
    //...
}
```

* Arguments definitions with no spaces:

```
public void setName(String name) {
    // ...
}

if (isTrue) {}

while (isTrue) {}
```

* No line length limitations

* Access modifiers first:

```
public static final String t1 = "";

public static transient final String t2 = "";

public static final transient String t3 = "";
```
