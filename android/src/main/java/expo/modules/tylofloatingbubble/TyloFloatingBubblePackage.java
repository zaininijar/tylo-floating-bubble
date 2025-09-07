package expo.modules.tylofloatingbubble;

import expo.modules.core.BasePackage;
import expo.modules.core.ExportedModule;

import java.util.Arrays;
import java.util.List;

public class TyloFloatingBubblePackage extends BasePackage {
    @Override
    public List<ExportedModule> createExportedModules(android.content.Context context) {
        return Arrays.<ExportedModule>asList(
            new TyloFloatingBubbleModule(context)
        );
    }
}
