# {{raw-name}}

## Start the Clojure REPL

In the shell:

```shell
lein repl :headless
```

In the Clojure REPL:

```clojure
(dev)
```

Now go to [http://localhost:3000/](http://localhost:3000/).

## Start the ClojureScript REPL

In the Clojure REPL:

```clojure
(cljs)
```

Then in the shell:

```node
node target/dev.js
```

## Run Tests

In the shell:

```shell
lein doo node 
```
