package base;

import models.visitors.Visitor;

import java.util.ArrayList;
import java.util.List;

public class TestContext {
    public static final ThreadLocal<TestContext> contexts = new ThreadLocal<>();
    private TestContext initializedContext;

    private List<Visitor> visitors = new ArrayList<>();
    private String testClassName;
    private String testMethodName;

    public TestContext(String testClassName, String testMethodName){
        this.testClassName = testClassName;
        this.testMethodName = testMethodName;
    }

    public static TestContext init(String testClassName, String testMethodName){
        TestContext context = new TestContext(testClassName, testMethodName);
        contexts.set(context);
        context.setContexts(context);
        return context;
    }

    private void setContexts(TestContext initializedContext) {
        this.initializedContext = initializedContext;
    }

    private TestContext getContext() {
        return this.initializedContext;
    }

    public String getTestMethodName() {
        return this.testMethodName;
    }

    public String getTestClassName() {
        return this.testClassName;
    }

    public List<Visitor> getVisitors() {
        return this.visitors;
    }

    public void addVisitor(Visitor visitor) {
        visitor.setTestContext(getContext());
        visitors.add(visitor);
    }

    public static TestContext get() {
        return contexts.get();
    }

    public static void remove() {
        contexts.remove();
    }

    public void whenTestStoppedByJvm(){
        // Do what you want when test stopped by jvm
    }

    public static void closeBrowsers() {
        if (TestContext.get() != null) {
            for (Visitor visitor : TestContext.get().getVisitors()) {
                if (visitor != null && visitor.browser() != null) {
                    visitor.closeBrowser();
                }
            }
        }
    }
}
