# Groovy, Gorm with Spring XD

### This piece covers the setup and use of Gorm (Grails ORM layer) as a stand-alone entity that can be used by Spring XD jobs and modules.

Foremost - **why**? What's Gorm and why use it with Spring XD?

Gorm is an extension to Hibernate in a way. Hibernate is good at ORM i.e. mapping classes to database objects. Hibernate is also good at querying. What Hibernate is not good at is the actual usage. There's simple too much you have to do get things going and too much noise afterwards. You have to annotate classes (or worse use XML descriptors), you have to setup persistent context, you have to use Hibernate session to execute actual queries. Now these are the areas addressed by Gorm. With Gorm you can use meaningful defaults. Let's have a look at this Groovy class

```java
class EntityName {
	String name
	String value
	String someOtherValue
	Date lastUpdated
	Date dateCreated
}
```

A class like this when seen by Gorm would automagically get converted into something similar

```java
@Entity
@Table (name = "entity_name")
class EntityName {
	@Id @GeneratedValue
    @Column(name = "id")
	int id

	@Version
	int version

    @Column(name = "name")
    String name

    @Column (name = "value")
    String value

    @Column (name = "some_other_value")
    String someOtherValue

    @Column (name = "last_update")
	Date lastUpdated

	@Column (name = "date_created")
	Date dateCreated


    @PrePersist
    void onCreate() {
       dateCreated = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
       lastUpdated = new Date();
    }
}
```

As you can see you can get a lot for free just by using Gorm. Now that's just a tip of the iceberg as with Gorm you also get validations, constraints and many, many more.

Hope it's clear now that it makes sense to use Gorm even outside of Grails. So what else you have to do to get Gorm running in Spring XD?

First of all, you have to get all Gorm dependency classes loaded by the initial classloader. To this end you can either copy all the classes into the lib folder (you don't really want to do that do you?) or tweak the startup script a bit and add another folder , say called `ext`, to added onto the classpath. See the attached patch for details.

Now with all the required classes on the classpath we can start playing with Gorm. First, we need to add it into our module's context.

```xml
<gorm:sessionFactory base-package="your.package" data-source-ref="dataSource"
                     message-source-ref="messageSource">
    <property name="hibernateProperties">
        <util:map>
            <entry key="hibernate.dialect" value="org.hibernate.dialect.MySQL5InnoDBDialect"/>
        </util:map>
    </property>
</gorm:sessionFactory>
```

Obviously you have to define the messageSource and dataSource (dataSource is normally already bound the one define in the main yaml file) and the messageSource is easy to define

```xml
<bean id="messageSource"
      class="org.springframework.context.support.ResourceBundleMessageSource">
    <property name="basename" value="messages"/>
</bean>
```

Just add all your messages files on the classpath (your module config will suffice). And that's pretty much it.

All your domain classes need to be annotated by the `import grails.persistence.Entity` annotation and have to implement equals and hash - for the lazy use the `import groovy.transform.EqualsAndHashCode` annotation and an AST transformation will take care of it for you.

There are several things you have to keep in mind should you wish to share the model classes between stand-alone Gorm and Grails' Gorm.

* Autostamping is not working (dateCreated, lastUpdated). You can always use the beforeUpdate closures though.
* You have to use session explicitly (DomainClass.withSession) or make a component using Gorms domain classes `org.springframework.transaction.annotation.Transactional`. In both cases you just make sure the Hibernate session is threadlocal.
* The Entity annotation adds (or more specifically AST adds) some extra fields to the domain classes. Gorm then tries to blindly maps those to the underlaying model and the fails. To avoid this add these fields into the transient section

```java
static transients = ['errors', 'instanceGormInstanceApi', 'instanceGormValidationApi',
'staticGormStaticApi', 'instanceConvertersApi', 'attached', 'dirty']
```


