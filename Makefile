#include ../../../Makefile.common
include ../Makefile.common
#
# Specfic options
#
ASTROMETRY_NAME	=astrometry
PACKAGEDIR 	=org/$(ESTAR_NAME)/$(ASTROMETRY_NAME)
PACKAGENAME	=org.$(ESTAR_NAME).$(ASTROMETRY_NAME)
JAR_FILE	=org_$(ESTAR_NAME)_$(ASTROMETRY_NAME).jar
JAVACFLAGS 	=$(JAVAC_VERSION_FLAGS) -d $(LIBDIR) -sourcepath ../../../ -classpath $(LIBDIR):$(CLASSPATH)
DOCSDIR 	= $(ESTAR_DOC_HOME)/javadocs/$(PACKAGEDIR)

SRCS = RA.java Dec.java CelestialObject.java CelestialObjectRMagnitudeComparator.java \
	CelestialObjectVMagnitudeComparator.java CelestialObjectRADecComparator.java
OBJS = $(SRCS:%.java=$(LIBDIR)/$(PACKAGEDIR)/%.class)
DOCS = $(SRCS:%.java=$(DOCSDIR)/$(PACKAGEDIR)/%.html)

DIRS = 

top: jar 

$(LIBDIR)/$(PACKAGEDIR)/%.class: %.java
	$(JAVAC) $(JAVAC_OPTIONS) $(JAVACFLAGS) $<
jar: $(JARLIBDIR)/$(JAR_FILE)

$(JARLIBDIR)/$(JAR_FILE): $(OBJS)
	(cd $(LIBDIR); $(JAR) $(JAR_OPTIONS) $(JAR_FILE) $(PACKAGEDIR); $(MV) $(JAR_FILE) $(JARLIBDIR))

docs: $(DOCS)

$(DOCSDIR)/$(PACKAGEDIR)/%.html: %.java
	$(JAVADOC) -sourcepath ../../..:$(CLASSPATH) -d $(DOCSDIR) $(DOCFLAGS) $(PACKAGENAME)

checkout:
	$(CO) $(CO_OPTIONS) $(SRCS)

checkin:
	-$(CI) $(CI_OPTIONS) $(SRCS)

depend:
	echo "No depend target."

clean:
	-$(RM) $(RM_OPTIONS) $(OBJS) $(TIDY_OPTIONS)

tidy:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)

backup: tidy checkin
	-$(RM) $(RM_OPTIONS) $(OBJS)
	$(TAR) cvf $(BACKUP_DIR)/org_$(ESTAR_NAME)_$(ASTROMETRY_NAME).tar .
	$(COMPRESS) $(BACKUP_DIR)/org_$(ESTAR_NAME)_$(ASTROMETRY_NAME).tar
