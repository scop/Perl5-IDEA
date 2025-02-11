/*
 * Copyright 2015-2023 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;

import java.util.Map;

public final class PerlDefaultInjectionMarkers {
  private PerlDefaultInjectionMarkers() {
  }

  public static final String PERL5_MARKER = "PERL5";

  public static final Map<String, String> DEFAULT_MARKERS = ContainerUtil.<String, String>immutableMapBuilder()
    .put("ACTIONSCRIPT", "ECMA Script Level 4")
    .put("ADB", "AndroidDataBinding")
    .put("AGSL", "AGSL")
    .put("AIDL", "AIDL")
    .put("ANGULAR", "AngularJS")
    .put("ANGULAR2", "ANGULAR2")
    .put("APACHE_CONFIG", "ApacheConfig")
    .put("APPLEJS", "Apple JS")
    .put("ASP", "Asp")
    .put("ASPECT_JS", "AspectJS")
    .put("ASTRO", "Astro")
    .put("AZURE", "AZURE")
    .put("BIGQUERY", "BigQuery")
    .put("BLADE", "Blade")
    .put("CASSANDRA", "CassandraQL")
    .put("CFML", "CFML")
    .put("CGO", "cgo")
    .put("CHAMELEON", "Chameleon")
    .put("CLICKHOUSE", "ClickHouse")
    .put("CMAKE", "CMake")
    .put("CMAKE_CACHE", "CMakeCache")
    .put("COCKROACH", "Cockroach")
    .put("COFFESCRIPT", "COFFEESCRIPT")
    .put("COMMAND_LINE", "CommandLine")
    .put("COMPOSER_LOG", "Composer Log")
    .put("CONCEPT", "Concept")
    .put("COOKIE", "Cookie")
    .put("COUCHBASE", "CouchbaseQuery")
    .put("CSS", "CSS")
    .put("CSS_VALUE", "CSS VALUE")
    .put("CYTHON", "Cython")
    .put("DART", "Dart")
    .put("DART_IN_HTML", "Dart in Html")
    .put("DATALORE", "Datalore")
    .put("DATALORE_PYTHON", "DatalorePython")
    .put("DB2", "DB2")
    .put("DB2IS", "DB2_IS")
    .put("DB2ZOS", "DB2_ZOS")
    .put("DERBY", "Derby")
    .put("DEVICE_SPEC", "DeviceSpec")
    .put("DJANGO", "Django")
    .put("DOCKERFILE", "Dockerfile")
    .put("DOXYGEN", "Doxygen")
    .put("DQL", "DQL")
    .put("DROOLS", "Drools")
    .put("DTD", "DTD")
    .put("ECMA6", "ECMAScript 6")
    .put("EDITOR_CONFIG", "EditorConfig")
    .put("EJS", "EJS")
    .put("EL", "EL")
    .put("EPP", "Embedded Puppet")
    .put("ERB", "RHTML")
    .put("EXASOL", "Exasol")
    .put("FACELETS", "Facelets")
    .put("FLOWJS", "Flow JS")
    .put("FREEMARKER", "FTL")
    .put("GDB", "GDB")
    .put("GHERKIN", "Gherkin")
    .put("GO", "go")
    .put("GOBUILD", "GoBuild")
    .put("GOFUZZ", "GoFuzzCorpus")
    .put("GOTPL", "GoTemplate")
    .put("GQL", "GQL")
    .put("GREENPLUM", "Greenplum")
    .put("GROOVY", "Groovy")
    .put("GSP", "GSP")
    .put("GSQL", "GenericSQL")
    .put("GWT", "GWT JavaScript")
    .put("GWTCSS", "GWT-CSS")
    .put("H2", "H2")
    .put("HAML", "Haml")
    .put("HBL", "Handlebars")
    .put("HCL", "HCL")
    .put("HEXDUMP", "HEXDUMP")
    .put("HIL", "HIL")
    .put("HIVEQL", "HiveQL")
    .put("HJSON", "HelmJSON")
    .put("HSQLDB", "HSQLDB")
    .put("HTEXT", "HelmTEXT")
    .put("HTML", "HTML")
    .put("HTTP", "HTTP Request")
    .put("HYAML", "HelmYAML")
    .put("IGNORE", "IgnoreLang")
    .put("INI", "Ini")
    .put("JADE", "Jade")
    .put("JAVA", "JAVA")
    .put("JINJA2", "Jinja2")
    .put("JKTMETA", "JKTMeta")
    .put("JPAQL", "JPAQL")
    .put("JQL", "JQL")
    .put("JQUERYCSS", "JQuery-CSS")
    .put("JS", "JavaScript")
    .put("JS15", "JavaScript 1.5")
    .put("JS16", "JavaScript 1.6")
    .put("JS17", "JavaScript 1.7")
    .put("JS18", "JavaScript 1.8")
    .put("JSHELL", "JShellLanguage")
    .put("JSON", "JSON")
    .put("JSONPATH", "JSONPath")
    .put("JSP", "JSP")
    .put("JSPX", "JSPX")
    .put("JSX", "JSX Harmony")
    .put("JUPYTERPYTHON", "JupyterPython")
    .put("JYPYTER", "Jupyter")
    .put("LESS", "LESS")
    .put("LINKER", "LinkerScript")
    .put("LIQUID", "liquid")
    .put("LLDB", "LLDB")
    .put("LOCALE", "Locale")
    .put("LOGCAT", "LogcatFilter")
    .put("LOMBOK", "Lombok.Config")
    .put("MAKEFILE", "Makefile")
    .put("MANIFEST", "Manifest")
    .put("MARIA", "MariaDB")
    .put("MARKDOWN", "Markdown")
    .put("MDX", "MDX")
    .put("MDXJS", "MdxJS")
    .put("METAJSON", "Metadata JSON")
    .put("MONGO", "MongoDB")
    .put("MONGOJS", "MongoJS")
    .put("MONGOJSON", "MongoDB-JSON")
    .put("MXML", "Mxml")
    .put("MYSQL", "MySQL")
    .put("NASHORNJS", "Nashorn JS")
    .put("OBJECTIVEC", "ObjectiveC")
    .put("OGNL", "OGNL")
    .put("OSQL", "Oracle")
    .put("OSQLP", "OracleSqlPlus")
    .put("PBX", "pbx")
    .put(PERL5_MARKER, PerlLanguage.INSTANCE.getID())
    .put("PGSQL", "PostgreSQL")
    .put("PHP", "InjectablePHP")
    .put("PHPT", "PHPT")
    .put("PLAN9", "plan9_x86")
    .put("PLAY", "Play")
    .put("PLIST", "Plist")
    .put("POSTCSS", "PostCSS")
    .put("PRISMA", "Prisma")
    .put("PROPERTIES", "Properties")
    .put("PROTOBUF", "protobuf")
    .put("PUPPET", "Puppet")
    .put("PYTHON", "Python")
    .put("QML", "QML")
    .put("QUARTO", "Quarto")
    .put("QUTE", "Qute")
    .put("R", "R")
    .put("RBS", "ruby.rbs")
    .put("RDOC", "RDOC")
    .put("REDIS", "Redis")
    .put("REDSHIFT", "Redshift")
    .put("REGEXP", "RegExp")
    .put("RELAXNG", "RELAX-NG")
    .put("RENDERSCRIPT", "Renderscript")
    .put("REST", "ReST")
    .put("RMARKDOWN", "RMarkdown")
    .put("ROOMSQL", "RoomSql")
    .put("ROXYGEN", "Roxygen")
    .put("RUBY", "ruby")
    .put("SASS", "SASS")
    .put("SCSS", "SCSS")
    .put("SHELL", "Shell Script")
    .put("SJS", "SvelteJS")
    .put("SLIM", "Slim")
    .put("SMALI", "Smali")
    .put("SMARTY", "Smarty")
    .put("SMARTYCONFIG", "SmartyConfig")
    .put("SNOWFLAKE", "Snowflake")
    .put("SPACEBARS", "Spacebars")
    .put("SPARKSQL", "SparkSQL")
    .put("SPEC", "Specification")
    .put("SPEL", "SpEL")
    .put("SPI", "SPI")
    .put("SQL", "SQL")
    .put("SQL92", "SQL92")
    .put("SQLITE", "SQLite")
    .put("STS", "SvelteTS")
    .put("STYLUS", "Stylus")
    .put("SVG", "SVG")
    .put("SWIFT", "Swift")
    .put("SYBASE", "Sybase")
    .put("TERRAFORM", "Terraform Config")
    .put("TEXT", "TEXT")
    .put("TEXTMATE", "textmate")
    .put("TOML", "TOML")
    .put("TS", "TypeScript")
    .put("TSJSX", "TypeScript JSX")
    .put("TSQL", "TSQL")
    .put("TWIG", "Twig")
    .put("VCPKG", "VCPKG")
    .put("VERTICA", "Vertica")
    .put("VGO", "vgo")
    .put("VJS", "VueJS")
    .put("VTL", "VTL")
    .put("VTS", "VueTS")
    .put("WERF", "WerfYAML")
    .put("XHTML", "XHTML")
    .put("XIB", "XIB")
    .put("XML", "XML")
    .put("XPATH", "XPath")
    .put("XPATH2", "XPath2")
    .put("XSLT", "$XSLT")
    .put("YAML", "yaml")
    .put("YOUTRACK", "YouTrack")
    .put("ZEPPELIN", "Zeppelin")
    .build();
}
