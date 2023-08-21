package club.lemos.leaf.plugin.annotation;

import club.lemos.leaf.plugin.LeafSpringBootStarterAutoConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LeafSpringBootStarterAutoConfigure.class)
@Inherited
public @interface EnableLeafServer {
}
