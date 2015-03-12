Build native code (only needed if src/com/litrik/eclipse/xps/core/LEDs.c was changed)
  1. Execute build/build.xml

Prepare the release:
  1. Update doc/changes.html
  1. Increase Bundle-Version in META-INF/MANIFEST.MF

Build/export the plug-in:
  1. Select "Export..." in context menu of the project
  1. Select "Deployable plug-ins and features"
  1. Click "Next >"
  1. Enter directory to export the plug-in

Release the new version of the plug-in:
  1. Upload the new release to [the download page](http://code.google.com/p/eclipse-xps/downloads/list) and label it as "Featured"
  1. Remove the "Featured" label from the previous release
  1. Update RecentChanges on the wiki
  1. Update the plug-in details at [Eclipse Plug-in Central](http://www.eclipseplugincentral.com/Web_Links-index-req-viewlink-cid-840.html)




