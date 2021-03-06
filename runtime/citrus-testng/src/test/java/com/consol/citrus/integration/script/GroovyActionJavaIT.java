/*
 * Copyright 2006-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.integration.script;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.TestNGCitrusSupport;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Test;

import static com.consol.citrus.script.GroovyAction.Builder.groovy;

/**
 * @author Christoph Deppisch
 */
@Test
public class GroovyActionJavaIT extends TestNGCitrusSupport {

    /** OS new line */
    private static final String NEWLINE = System.getProperty("line.separator");

    @CitrusTest
    public void groovyAction() {
        variable("date", "citrus:currentDate()");
        variable("greetingText", "Hello Citrus!");

        description("This example executes groovy scripts using both inline script definition and external file resource.");

        run(groovy("println 'Hello Citrus'"));

        run(groovy("println 'Current date is ${date}!'"));

        run(groovy("import com.consol.citrus.*" + NEWLINE +
                "import com.consol.citrus.variable.*" + NEWLINE +
                "import com.consol.citrus.context.TestContext" + NEWLINE +
                "import com.consol.citrus.script.GroovyAction.ScriptExecutor" + NEWLINE +
                "import org.testng.Assert" + NEWLINE +
                "public class GScript implements ScriptExecutor {" + NEWLINE +
                    "public void execute(TestContext context) {" + NEWLINE +
                        "Assert.assertEquals(context.getVariable(\"greetingText\"), \"Hello Citrus!\")" + NEWLINE +
                        "Assert.assertEquals(context.getVariable(\"greetingText\"), \"${greetingText}\")" + NEWLINE +
                    "}" + NEWLINE +
                "}"));

        run(groovy("println context.getVariable(\"date\")" + NEWLINE +
                "assert context.getVariable(\"greetingText\").equals(\"Hello Citrus!\")" + NEWLINE +
                "assert context.getVariable(\"greetingText\").equals(\"${greetingText}\")"));

        run(groovy("println 'Hello Citrus'").skipTemplate());

        run(groovy("Assert.assertEquals(context.getVariable(\"scriptTemplateVar\"), \"It works!\")" + NEWLINE +
                "Assert.assertEquals(context.getVariable(\"greetingText\"), \"Hello Citrus!\")" + NEWLINE +
                "Assert.assertEquals(context.getVariable(\"greetingText\"), \"${greetingText}\")")
                .template("classpath:com/consol/citrus/integration/script/custom-script-template.groovy"));

        run(groovy("import org.testng.Assert" + NEWLINE +
                  "Assert.assertEquals(context.getVariable(\"scriptTemplateVar\"), \"It works!\")" + NEWLINE +
                  "Assert.assertEquals(context.getVariable(\"greetingText\"), \"Hello Citrus!\")" + NEWLINE +
                  "Assert.assertEquals(context.getVariable(\"greetingText\"), \"${greetingText}\")"));

        run(groovy("public class MyCustomClass {" + NEWLINE +
                    "public void run() {" + NEWLINE +
                        "println 'Just executed a custom class with run method!'" + NEWLINE +
                    "}" + NEWLINE +
                  "}"));

        run(groovy(new ClassPathResource("com/consol/citrus/integration/script/example.groovy")));
    }
}
