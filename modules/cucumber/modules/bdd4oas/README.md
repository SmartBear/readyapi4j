## BDD4OAS ReadyAPI4j module

This module allows for inline definitions of when / then vocabularies inside an OAS definition, which 
can then be used to write and execute Cucumber scenarios without any further requirement for writing
any StepDef code. The vocabularies are added using custom OAS extensions and dynamically processed when
executing corresponding scenarios using the runner in this module.

### A simple example

A quick example - consider the following minimal OAS 3.0 definition:

```

```

We would like to write a scenario to test this API as follows:

```

```

Traditionally this would require us to write corresponding StepDefs in our language of choice and 
execute them with cucumber. Instead, this module allows us to define the above when/then
vocabulary inside the OAS definition using x-bdd extensions:

```

``` 

As you can see - an x-bdd-when extension has been added to the operation, and a corresponding 
x-bdd-then extension has been added to the default response. A given statement has also been added
to point to the OAS definition itself, which could be hosted wherever you want (in this example it's 
hosted as a public specifiction on SwaggerHub).

Now all we have to do is run the above scenario - no coding required!

```

```

### Creating parameterized x-bdd-when vocabularies

The above example is straight-forward - but what if we want to define a specific vocabulary to 
call the operation with specific input values? We can do this as follows:

```
``` 

Now we can use the defined x-bdd-when statements in our scenario, and the bdd4oas runner will 
automatically set the defined properties as defined when making the call.

### Adding assertions to x-bdd-then vocabularies

It's pretty common to want to assert an API response for specific values or content, which is
traditionally done in code/tooling using an assertion mechanism. Fortunately this is possible here
as well:

```
```

As you can see, the ... x-bdd-then definition asserts the response for specific content using 
a JSON-Path expression - which allows us for functional validation of the API being called. bdd4oas 
supports a number of assertions - see the entire list below.

### Using the standard REST / OAS vocabularies

The default vocabularies for testing REST APIs provided by ReadyAPI4j are still at your disposal
when writing your scenarios - so you can intermix these with your custom definitions provided 
via x-bdd extensions. For example we could add another scenario to our above example:

```

``` 

### Adding your own StepDefs

Last but not least - you can still intermix all the above with your custom StepDefs writtin in Java






## Assertion reference

### json assertions


### xml assertions


### content assertions