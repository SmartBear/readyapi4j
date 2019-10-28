# Property transfers - Transferring content in ReadyAPI4j DSL

One of the most powerful features of [SoapUI](https://www.soapui.org) is the ability to easily transfer values from one test step
to another one. This feature is known as ***Property transfer***. It is most often used to extract some part of it 
and pass it into a subsequent request.

Here's a simple example where we use JSONPath to replace the value ```WILL_BE_REPLACED``` with a customer ID
extracted from the last response.

```groovy
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
 
 executeRecipe {
    get 'https://staging-server/customers/1'
    transfer '$.customerId' to '$.customerId' of request
    post 'https://staging-server/orders', '{ "customerId": "WILL_BE_REPLACED", "item": "ABC-123"}'
 }
 ```

Property transfers understand both JSONPath and XPath expressions. You also don't have to specify a path - instead you 
replace a property completely. See [Choosing the target property](#choose_property), below.

## Choosing the target request
 
If you want to transfer the value into a step other than the next one, you need to name the step in question.
This is done with the ```name``` attribute in the configuration block of the request. In the code below,
the POST request is named ```createOrder```, and this name is used by the two property transfer step to ensure that
the correct parts of the posts are modified.

```groovy
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
 
 executeRecipe {
    get 'https://staging-server/customers/1'
    transfer '$.customerId' to '$.customerId' of step: 'createOrder'
    get 'https://staging-server/products/1'
    transfer '$.id' to '$.item' of step: 'createOrder'
    wait 1 second 
    post 'https://staging-server/orders', {
        name 'createOrder'
        body '{ "customerId": "WILL_BE_REPLACED", "item": "WILL_BE_REPLACED"}'
    }
 }
 ```

## <a name="choose_property"></a>Choosing the target property

In all examples above, property transfers were used to replace part of a POST. However, the value collected
from the source step can also completely replace a property or parameter. This feature is especially useful
if you have a basic understanding of how test steps and their properties interact in SoapUI.

This code snippet demonstrates how to insert values into the Username and Password properties of the 
next request:
```groovy
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
 
 executeRecipe {
    get 'https://staging-server/customers/1/credentials'
    transfer '$.userName' to step: 'createOrder', property: 'Username'
    transfer '$.password' to step: 'createOrder', property: 'Password'
    post 'https://staging-server/orders', {
        name 'createOrder'
        body '{ "customerId": "JonSnow", "item": "ABC-123"}'
    }
 }
 ```
What properties are available for modification depends on the type of the step. For instance, 
[this page](https://support.smartbear.com/readyapi/docs/projects/ui/request-properties/rest.html) lists the properties 
of REST request steps; [this page](https://support.smartbear.com/readyapi/docs/projects/ui/request-properties/soap.html)
documents what's available for SOAP requests.

In addition to the standard properties, all parameters defined for a request will also be available for modification.
In other words, if you have defined the parameter ```userName``` in the request named ```findOrder```, this property 
transfer will modify it:
```groovy
transfer '$.password' to step: 'findOrder', property: 'userName'
```

This is a good way of transfering content into HTTP headers, as demonstrated in this code sample:
```groovy
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
 
 executeRecipe {
    get 'https://staging-server/session_cookies/1'
    transfer '$.cookieValue' to step: 'createOrder', property: 'Set-Cookie'
    post 'https://staging-server/orders', {
        name 'createOrder'
        body '{ "customerId": "JonSnow", "item": "ABC-123"}'
        parameters {
            header 'Set-Cookie', 'WILL_BE_REPLACED'
        }
    }
 }
 ```

## Choosing the source of a transfer
 
Although this is a less common scenario, sometimes it's useful to specify not the target, but ***from*** what step 
and property you are transferring content. The syntax for this is similar to specifying the target, but
for technical reasons you need to put the source and property parameters in parentheses.

```groovy
import static com.smartbear.readyapi4j.dsl.execution.RecipeExecution.executeRecipe
 
 executeRecipe {
    post 'https://staging-server/orders', {
         name 'createOrder'
         body '{ "customerId": "JonSnow", "item": "ABC-123"}'
    }
    transfer (step: 'createOrder', property: 'Response') to '$.lastOrderStatus' of request
    put 'https://staging-server/customers/1/order_status', {
        body '{ "lastOrderStatus": "WILL_BE_REPLACED"}'
    }
 }
 ```