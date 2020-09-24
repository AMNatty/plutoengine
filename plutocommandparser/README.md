# plutoengine:plutocommandparser

PlutoEngine's command parser.

## Description 

PlutoCommandParser is an attempt to streamline my previous attempts at command parsers.
Its main goal is to provide a modular and flexible tokenizer, parser and evaluator
for a simple user-friendly CLI-like language called `PlutoCmd`.

## Goals

* High syntax error tolerance
* Provide implementations for basic Java types, such as primitives and Strings
* Allow extensibility while providing a strong foundation
* Complete user control over localization, no hardcoded Strings

## Non-goals

PlutoCmd is *not* a replacement for standard scripting languages and will most likely
never be a Turing-complete language.


## Implementation style

### Command implementation

Each command has its own *final* class, abstractions over CommandBase are howered allowed. 
Note the usage of the `ConstantExpression` annotation over some interface methods. These 
annotations **must** be respected - methods must be stateless and deterministic.

## PlutoCmd language specification

### General syntax

```
[prefix]command [arg1] [arg2] ... [argN]
```

`prefix` is an optional identifier String to distinguish PlutoCmd commands from other commands.

`command` is an alias for a command, handled by one of the command's handlers 

`argX` is an argument of the invoked command-function, optionally quoted to preserve whitespace.

