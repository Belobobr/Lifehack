package md.fusionworks.lifehack.di

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.RUNTIME
import javax.inject.Scope

@Scope
@Retention(RUNTIME)
annotation class PerActivity
