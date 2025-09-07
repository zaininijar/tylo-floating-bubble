package expo.modules.tylofloatingbubble;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import expo.modules.core.ExportedModule;
import expo.modules.core.ModuleRegistry;
import expo.modules.core.Promise;
import expo.modules.core.arguments.ReadableArguments;
import expo.modules.core.interfaces.ExpoMethod;
import expo.modules.core.interfaces.services.EventEmitter;

public class TyloFloatingBubbleModule extends ExportedModule {
    private FloatingBubbleService floatingBubbleService;
    private boolean isServiceRunning = false;
    private ModuleRegistry moduleRegistry;
    private EventEmitter eventEmitter;
    private static TyloFloatingBubbleModule instance;

    public TyloFloatingBubbleModule(Context context) {
        super(context);
        instance = this;
    }

    @Override
    public String getName() {
        return "TyloFloatingBubble";
    }

    // Initialize module registry and event emitter
    public void initialize(ModuleRegistry moduleRegistry) {
        this.moduleRegistry = moduleRegistry;
        this.eventEmitter = moduleRegistry.getModule(EventEmitter.class);
    }

    @ExpoMethod
    public void initialize(Promise promise) {
        try {
            // This method is called from React Native to initialize the module
            android.util.Log.d("FloatingBubble", "Module initialize called from React Native");
            promise.resolve("Module initialized");
        } catch (Exception e) {
            promise.reject("INIT_ERROR", "Failed to initialize module: " + e.getMessage());
        }
    }

    // Static method to send event from service
    public static void sendBubbleClickEvent() {
        try {
            android.util.Log.d("FloatingBubble", "sendBubbleClickEvent called");
            if (instance != null) {
                android.util.Log.d("FloatingBubble", "Instance found");
                if (instance.eventEmitter != null) {
                    android.util.Log.d("FloatingBubble", "EventEmitter found, sending event");
                    instance.eventEmitter.emit("onBubbleClick", null);
                } else {
                    android.util.Log.e("FloatingBubble", "EventEmitter is null");
                }
            } else {
                android.util.Log.e("FloatingBubble", "Instance is null");
            }
        } catch (Exception e) {
            android.util.Log.e("FloatingBubble", "Failed to send bubble click event: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @ExpoMethod
    public void checkBubblePermission(Promise promise) {
        try {
            boolean hasPermission;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hasPermission = Settings.canDrawOverlays(getContext());
            } else {
                hasPermission = true;
            }
            promise.resolve(hasPermission);
        } catch (Exception e) {
            promise.reject("ERROR", "Failed to check permission", e);
        }
    }

    @ExpoMethod
    public void requestBubblePermission(Promise promise) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(getContext())) {
                    Intent intent = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getContext().getPackageName())
                    );
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                    promise.resolve(false); // Permission not granted yet, user needs to grant it manually
                } else {
                    promise.resolve(true); // Permission already granted
                }
            } else {
                promise.resolve(true); // No permission needed for older Android versions
            }
        } catch (Exception e) {
            promise.reject("ERROR", "Failed to request permission", e);
        }
    }

    @ExpoMethod
    public void showBubble(ReadableArguments data, Promise promise) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
                promise.resolve(false); // No permission
                return;
            }

            Intent intent = new Intent(getContext(), FloatingBubbleService.class);
            // Bubble params
            intent.putExtra("title", data.getString("title", "New Order"));
            intent.putExtra("subtitle", data.getString("subtitle", "Tap to view details"));
            intent.putExtra("showBadge", data.getBoolean("showBadge", true));
            intent.putExtra("badgeCount", data.getInt("badgeCount", 1));
            intent.putExtra("icon", data.getString("icon", "tylo_circle"));
            
            // Debug: Check what data we actually receive
            android.util.Log.d("FloatingBubble", "Module - Raw data: " + data.toString());
            
            // Popup params - get from data
            String popupTitle = data.getString("popupTitle", "New Ride Request");
            String popupPrice = data.getString("popupPrice", "$24.50");
            String popupDuration = data.getString("popupDuration", "15 min");
            String popupDistance = data.getString("popupDistance", "8.2 km");
            String popupPickupAddress = data.getString("popupPickupAddress", "123 Main Street, Downtown Area");
            String popupDestinationAddress = data.getString("popupDestinationAddress", "456 Business Center, Tech District");
            
            android.util.Log.d("FloatingBubble", "Module - Popup Title: " + popupTitle);
            android.util.Log.d("FloatingBubble", "Module - Popup Price: " + popupPrice);
            android.util.Log.d("FloatingBubble", "Module - Popup Duration: " + popupDuration);
            android.util.Log.d("FloatingBubble", "Module - Popup Distance: " + popupDistance);
            android.util.Log.d("FloatingBubble", "Module - Popup Pickup: " + popupPickupAddress);
            android.util.Log.d("FloatingBubble", "Module - Popup Destination: " + popupDestinationAddress);
            
            intent.putExtra("popupTitle", popupTitle);
            intent.putExtra("popupSubtitle", data.getString("popupSubtitle", "Tap to view details"));
            intent.putExtra("popupPrice", popupPrice);
            intent.putExtra("popupDuration", popupDuration);
            intent.putExtra("popupDistance", popupDistance);
            intent.putExtra("popupPickupTitle", data.getString("popupPickupTitle", "Pickup Location"));
            intent.putExtra("popupPickupAddress", popupPickupAddress);
            intent.putExtra("popupDestinationTitle", data.getString("popupDestinationTitle", "Destination"));
            intent.putExtra("popupDestinationAddress", popupDestinationAddress);
            intent.putExtra("popupPaymentMethod", data.getString("popupPaymentMethod", "Cash"));
            intent.putExtra("popupAcceptText", data.getString("popupAcceptText", "Accept"));
            intent.putExtra("popupRejectText", data.getString("popupRejectText", "Reject"));
            
            getContext().startService(intent);
            isServiceRunning = true;
            
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject("ERROR", "Failed to show bubble", e);
        }
    }

    @ExpoMethod
    public void hideBubble(Promise promise) {
        try {
            Intent intent = new Intent(getContext(), FloatingBubbleService.class);
            getContext().stopService(intent);
            isServiceRunning = false;
            
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject("ERROR", "Failed to hide bubble", e);
        }
    }

    @ExpoMethod
    public void isBubbleVisible(Promise promise) {
        try {
            promise.resolve(isServiceRunning);
        } catch (Exception e) {
            promise.reject("ERROR", "Failed to check visibility", e);
        }
    }
}
