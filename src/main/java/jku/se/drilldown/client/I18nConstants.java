package jku.se.drilldown.client;

import com.google.gwt.core.client.GWT;

public interface I18nConstants extends com.google.gwt.i18n.client.Constants {

  static I18nConstants INSTANCE = GWT.create(I18nConstants.class);

  @DefaultStringValue("Hier wird das QM Drilldown Modul entwickelt!")
  String sample();
  @DefaultStringValue("Hier wird das QM Drilldown Config Modul entwickelt!")
  String sampleConfig();
}
