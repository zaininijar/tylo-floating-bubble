package expo.modules.tylofloatingbubble;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import expo.modules.core.ModuleRegistry;
import expo.modules.core.interfaces.services.EventEmitter;

public class FloatingBubbleService extends Service {
    private WindowManager windowManager;
    private View floatingView;
    private boolean isExpanded = false;
    private EventEmitter eventEmitter;
    
    // Store popup data for later use - hardcoded for testing
    private String popupTitle = "New Ride Request";
    private String popupSubtitle = "Tap to view details";
    private String popupPrice = "$24.50";
    private String popupDuration = "15 min";
    private String popupDistance = "8.2 km";
    private String popupPickupTitle = "Lokasi Penjemputan";
    private String popupPickupAddress = "123 Main Street, Downtown Area";
    private String popupDestinationTitle = "Tujuan";
    private String popupDestinationAddress = "456 Business Center, Tech District";
    private String popupPaymentMethod = "Credit Card";
    private String popupAcceptText = "Terima Perjalanan";
    private String popupRejectText = "Tolak";
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        // Get event emitter from module registry
        try {
            // This is a simplified approach - in a real implementation you'd get this from the module
            eventEmitter = null; // Will be set by the module when needed
        } catch (Exception e) {
            android.util.Log.e("FloatingBubble", "Failed to get event emitter: " + e.getMessage());
        }
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        android.util.Log.d("FloatingBubble", "onStartCommand called");
        if (floatingView == null) {
            android.util.Log.d("FloatingBubble", "Creating floating bubble");
            createFloatingBubble(intent);
        } else {
            android.util.Log.d("FloatingBubble", "Bubble already exists");
        }
        return START_STICKY;
    }
    
    private void createFloatingBubble(Intent intent) {
        // Basic bubble parameters
        String title = intent != null ? intent.getStringExtra("title") : "New Order";
        String subtitle = intent != null ? intent.getStringExtra("subtitle") : "Tap to view details";
        boolean showBadge = intent != null ? intent.getBooleanExtra("showBadge", true) : true;
        int badgeCount = intent != null ? intent.getIntExtra("badgeCount", 1) : 1;
        String iconName = intent != null ? intent.getStringExtra("icon") : "tylo_circle";
        
        // Store popup data from intent
        if (intent != null) {
            // Try to get popup data from intent
            String intentPopupTitle = intent.getStringExtra("popupTitle");
            String intentPopupPrice = intent.getStringExtra("popupPrice");
            String intentPopupDuration = intent.getStringExtra("popupDuration");
            String intentPopupDistance = intent.getStringExtra("popupDistance");
            String intentPopupPickupAddress = intent.getStringExtra("popupPickupAddress");
            String intentPopupDestinationAddress = intent.getStringExtra("popupDestinationAddress");
            
            android.util.Log.d("FloatingBubble", "Intent popupTitle: " + intentPopupTitle);
            android.util.Log.d("FloatingBubble", "Intent popupPrice: " + intentPopupPrice);
            android.util.Log.d("FloatingBubble", "Intent popupDuration: " + intentPopupDuration);
            android.util.Log.d("FloatingBubble", "Intent popupDistance: " + intentPopupDistance);
            android.util.Log.d("FloatingBubble", "Intent popupPickupAddress: " + intentPopupPickupAddress);
            android.util.Log.d("FloatingBubble", "Intent popupDestinationAddress: " + intentPopupDestinationAddress);
            
            // Use intent data if available, otherwise use hardcoded defaults
            if (intentPopupTitle != null && !intentPopupTitle.isEmpty()) {
                popupTitle = intentPopupTitle;
            }
            if (intentPopupPrice != null && !intentPopupPrice.isEmpty()) {
                popupPrice = intentPopupPrice;
            }
            if (intentPopupDuration != null && !intentPopupDuration.isEmpty()) {
                popupDuration = intentPopupDuration;
            }
            if (intentPopupDistance != null && !intentPopupDistance.isEmpty()) {
                popupDistance = intentPopupDistance;
            }
            if (intentPopupPickupAddress != null && !intentPopupPickupAddress.isEmpty()) {
                popupPickupAddress = intentPopupPickupAddress;
            }
            if (intentPopupDestinationAddress != null && !intentPopupDestinationAddress.isEmpty()) {
                popupDestinationAddress = intentPopupDestinationAddress;
            }
        }
        
        android.util.Log.d("FloatingBubble", "Final popup data:");
        android.util.Log.d("FloatingBubble", "Popup Title: " + popupTitle);
        android.util.Log.d("FloatingBubble", "Popup Price: " + popupPrice);
        android.util.Log.d("FloatingBubble", "Popup Duration: " + popupDuration);
        android.util.Log.d("FloatingBubble", "Popup Distance: " + popupDistance);
        android.util.Log.d("FloatingBubble", "Popup Pickup: " + popupPickupAddress);
        android.util.Log.d("FloatingBubble", "Popup Destination: " + popupDestinationAddress);
        
        // Get screen dimensions
        android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        
        // Create bubble layout
        RelativeLayout bubbleLayout = new RelativeLayout(this);
        bubbleLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(180, 180));
        
        // Create icon ImageView
        ImageView iconView = new ImageView(this);
        RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(140, 140);
        iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        iconView.setLayoutParams(iconParams);
        iconView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        
        // Set icon with fallback background color
        try {
            int resourceId = getResources().getIdentifier(iconName, "drawable", getPackageName());
            if (resourceId != 0) {
                iconView.setImageResource(resourceId);
            } else {
                // Fallback to colored background
                GradientDrawable fallbackIcon = new GradientDrawable();
                fallbackIcon.setShape(GradientDrawable.OVAL);
                fallbackIcon.setColor(Color.parseColor("#FF6B35")); // Orange color
                iconView.setBackground(fallbackIcon);
                iconView.setImageResource(android.R.drawable.ic_dialog_info); // Default Android icon
            }
        } catch (Exception e) {
            // Fallback to colored background
            GradientDrawable fallbackIcon = new GradientDrawable();
            fallbackIcon.setShape(GradientDrawable.OVAL);
            fallbackIcon.setColor(Color.parseColor("#FF6B35")); // Orange color
            iconView.setBackground(fallbackIcon);
            iconView.setImageResource(android.R.drawable.ic_dialog_info); // Default Android icon
        }
        
        bubbleLayout.addView(iconView);
        
        // Create badge if needed
        TextView badge = null;
        if (showBadge && badgeCount > 0) {
            badge = new TextView(this);
            badge.setText(badgeCount > 99 ? "99+" : String.valueOf(badgeCount));
            badge.setTextColor(Color.WHITE);
            badge.setTextSize(9f);
            badge.setGravity(Gravity.CENTER);
            badge.setIncludeFontPadding(false);
            badge.setPadding(6, 2, 6, 2);
            
            // Position badge directly on top-right of icon
            RelativeLayout.LayoutParams badgeParams = new RelativeLayout.LayoutParams(24, 24);
            badgeParams.addRule(RelativeLayout.ALIGN_TOP, iconView.getId());
            badgeParams.addRule(RelativeLayout.ALIGN_END, iconView.getId());
            // setMargins(left, top, right, bottom)
            // Kiri, Atas, Kanan, Bawah
            badgeParams.setMargins(6, 6, 0, 0); // kiri: -12, atas: -12, kanan: 0, bawah: 0
            badge.setLayoutParams(badgeParams);
            
            // Badge background
            GradientDrawable badgeBackground = new GradientDrawable();
            badgeBackground.setColor(Color.parseColor("#FF4444"));
            badgeBackground.setCornerRadius(12f); // Half of width/height for perfect circle
            badge.setBackground(badgeBackground);
            
            bubbleLayout.addView(badge);
        }
        
        floatingView = bubbleLayout;
        
        // Set up window parameters
        int layoutFlag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            : WindowManager.LayoutParams.TYPE_PHONE;
        
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        
        // Position bubble at right center of screen initially
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = screenWidth - 56 - 20; // Right side with 20px margin
        params.y = (screenHeight - 56) / 2; // Center vertically
        
        // Add touch handling
        int[] initialX = {0};
        int[] initialY = {0};
        float[] initialTouchX = {0f};
        float[] initialTouchY = {0f};
        boolean[] isDragging = {false};
        
        bubbleLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX[0] = params.x;
                        initialY[0] = params.y;
                        initialTouchX[0] = event.getRawX();
                        initialTouchY[0] = event.getRawY();
                        isDragging[0] = false;
                        return true;
                        
                    case MotionEvent.ACTION_MOVE:
                        if (isExpanded) {
                            return true;
                        }
                        
                        float deltaX = event.getRawX() - initialTouchX[0];
                        float deltaY = event.getRawY() - initialTouchY[0];
                        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                        
                        if (distance > 15) {
                            isDragging[0] = true;
                            
                            params.x = initialX[0] + (int) deltaX;
                            params.y = initialY[0] + (int) deltaY;
                            
                            // Keep bubble within screen bounds
                            params.x = Math.max(0, Math.min(params.x, screenWidth - 100));
                            params.y = Math.max(0, Math.min(params.y, screenHeight - 100));
                            
                            windowManager.updateViewLayout(floatingView, params);
                        }
                        return true;
                        
                    case MotionEvent.ACTION_UP:
                        if (!isDragging[0]) {
                            // Quick tap - add bubble click animation then show popup
                            animateBubbleClick();
                        } else {
                            // Snap to edge
                            snapToEdge(params);
                        }
                        return true;
                        
                    default:
                        return false;
                }
            }
        });
        
        try {
            windowManager.addView(floatingView, params);
            android.util.Log.d("FloatingBubble", "Bubble added successfully to window manager");
        } catch (Exception e) {
            android.util.Log.e("FloatingBubble", "Failed to add bubble to window manager: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void snapToEdge(WindowManager.LayoutParams params) {
        android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        int bubbleWidth = 100;
        
        // Determine which edge is closest
        int distanceToLeft = params.x;
        int distanceToRight = screenWidth - params.x - bubbleWidth;
        int distanceToTop = params.y;
        int distanceToBottom = screenHeight - params.y - bubbleWidth;
        
        int minDistance = Math.min(Math.min(distanceToLeft, distanceToRight), 
                                 Math.min(distanceToTop, distanceToBottom));
        
        int targetX, targetY;
        
        if (minDistance == distanceToLeft) {
            targetX = 20;
            targetY = Math.max(50, Math.min(params.y, screenHeight - 150));
        } else if (minDistance == distanceToRight) {
            targetX = screenWidth - bubbleWidth - 20;
            targetY = Math.max(50, Math.min(params.y, screenHeight - 150));
        } else if (minDistance == distanceToTop) {
            targetX = Math.max(50, Math.min(params.x, screenWidth - 150));
            targetY = 50;
        } else { // distanceToBottom
            targetX = Math.max(50, Math.min(params.x, screenWidth - 150));
            targetY = screenHeight - bubbleWidth - 50;
        }
        
        // Smooth animation to edge
        animateToPosition(targetX, targetY, params);
    }
    
    private void animateToPosition(int targetX, int targetY, WindowManager.LayoutParams params) {
        try {
            // Create smooth animation using ValueAnimator
            android.animation.ValueAnimator animatorX = android.animation.ValueAnimator.ofInt(params.x, targetX);
            android.animation.ValueAnimator animatorY = android.animation.ValueAnimator.ofInt(params.y, targetY);
            
            animatorX.setDuration(300); // 300ms animation
            animatorY.setDuration(300);
            
            animatorX.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                    params.x = (int) animation.getAnimatedValue();
                    try {
                        windowManager.updateViewLayout(floatingView, params);
                    } catch (Exception e) {
                        android.util.Log.e("FloatingBubble", "Animation update failed: " + e.getMessage());
                    }
                }
            });
            
            animatorY.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                    params.y = (int) animation.getAnimatedValue();
                    try {
                        windowManager.updateViewLayout(floatingView, params);
                    } catch (Exception e) {
                        android.util.Log.e("FloatingBubble", "Animation update failed: " + e.getMessage());
                    }
                }
            });
            
            // Start animations
            animatorX.start();
            animatorY.start();
            
            android.util.Log.d("FloatingBubble", "Animating bubble to position: " + targetX + ", " + targetY);
            
        } catch (Exception e) {
            android.util.Log.e("FloatingBubble", "Animation failed, using direct positioning: " + e.getMessage());
            // Fallback to direct positioning
            params.x = targetX;
            params.y = targetY;
            windowManager.updateViewLayout(floatingView, params);
        }
    }
    
    private void animateBubbleClick() {
        try {
            // Animate bubble scale down and up
            floatingView.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        floatingView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    sendBubbleClickEvent();
                                }
                            })
                            .start();
                    }
                })
                .start();
        } catch (Exception e) {
            android.util.Log.e("FloatingBubble", "Failed to animate bubble click: " + e.getMessage());
            // Fallback to direct call
            sendBubbleClickEvent();
        }
    }

    private void sendBubbleClickEvent() {
        try {
            android.util.Log.d("FloatingBubble", "Bubble clicked - showing popup overlay");
            // Show popup overlay directly instead of sending event to React Native
            showPopupOverlay(null);
            
        } catch (Exception e) {
            android.util.Log.e("FloatingBubble", "Failed to handle bubble click: " + e.getMessage());
        }
    }

    private void showPopupOverlay(Intent intent) {
        try {
            // Get screen dimensions
            android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;
            int popupWidth = (int) (screenWidth * 0.9); // 90% of screen width
            
            // Create popup overlay window
            android.view.WindowManager.LayoutParams popupParams = new android.view.WindowManager.LayoutParams(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O 
                    ? android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    : android.view.WindowManager.LayoutParams.TYPE_PHONE,
                android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                android.graphics.PixelFormat.TRANSLUCENT
            );

            // Create popup layout with semi-transparent background
            android.widget.RelativeLayout popupLayout = new android.widget.RelativeLayout(this);
            popupLayout.setBackgroundColor(android.graphics.Color.parseColor("#80000000")); // Semi-transparent background

            // Create main popup content container
            android.widget.LinearLayout contentLayout = new android.widget.LinearLayout(this);
            contentLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
            contentLayout.setPadding(24, 24, 24, 24);
            
            // Create rounded background with shadow (matching React Native design)
            android.graphics.drawable.GradientDrawable background = new android.graphics.drawable.GradientDrawable();
            background.setColor(android.graphics.Color.WHITE);
            background.setCornerRadius(24); // Rounded corners like React Native
            
            // Add shadow effect
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                contentLayout.setElevation(12);
            }
            
            contentLayout.setBackground(background);
            
            // Set layout params for content - position in center
            android.widget.RelativeLayout.LayoutParams contentParams = new android.widget.RelativeLayout.LayoutParams(
                popupWidth,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            
            // Position popup in center of screen
            contentParams.addRule(android.widget.RelativeLayout.CENTER_IN_PARENT);

            // HEADER SECTION (matching React Native design)
            android.widget.LinearLayout headerLayout = new android.widget.LinearLayout(this);
            headerLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
            headerLayout.setGravity(android.view.Gravity.CENTER);
            android.widget.LinearLayout.LayoutParams headerParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            headerParams.setMargins(0, 0, 0, 20);

            // Title
            android.widget.TextView titleView = new android.widget.TextView(this);
            titleView.setText(popupTitle);
            titleView.setTextSize(20);
            titleView.setTextColor(android.graphics.Color.parseColor("#1F2937"));
            titleView.setTypeface(null, android.graphics.Typeface.BOLD);
            titleView.setGravity(android.view.Gravity.CENTER);
            android.widget.LinearLayout.LayoutParams titleParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            titleParams.setMargins(0, 0, 0, 8);
            headerLayout.addView(titleView, titleParams);

            // Subtitle
            android.widget.TextView subtitleView = new android.widget.TextView(this);
            subtitleView.setText(popupSubtitle);
            subtitleView.setTextSize(14);
            subtitleView.setTextColor(android.graphics.Color.parseColor("#6B7280"));
            subtitleView.setGravity(android.view.Gravity.CENTER);
            headerLayout.addView(subtitleView);

            contentLayout.addView(headerLayout, headerParams);

            // PRICE CARD SECTION (green background like React Native)
            android.widget.LinearLayout priceCardLayout = new android.widget.LinearLayout(this);
            priceCardLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
            priceCardLayout.setGravity(android.view.Gravity.CENTER);
            priceCardLayout.setPadding(24, 24, 24, 24);
            
            // Create rounded background for price card
            android.graphics.drawable.GradientDrawable priceCardBackground = new android.graphics.drawable.GradientDrawable();
            priceCardBackground.setColor(android.graphics.Color.parseColor("#10B981"));
            priceCardBackground.setCornerRadius(16);
            priceCardLayout.setBackground(priceCardBackground);

            android.widget.LinearLayout.LayoutParams priceCardParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            priceCardParams.setMargins(0, 0, 0, 20);

            // Price text
            android.widget.TextView priceView = new android.widget.TextView(this);
            priceView.setText(popupPrice);
            priceView.setTextSize(28);
            priceView.setTextColor(android.graphics.Color.WHITE);
            priceView.setTypeface(null, android.graphics.Typeface.BOLD);
            priceView.setGravity(android.view.Gravity.CENTER);
            android.widget.LinearLayout.LayoutParams priceParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            priceParams.setMargins(0, 0, 0, 8);
            priceCardLayout.addView(priceView, priceParams);

            // Time and distance
            android.widget.TextView durationDistanceView = new android.widget.TextView(this);
            durationDistanceView.setText("⏱ " + popupDuration + " • " + popupDistance);
            durationDistanceView.setTextSize(14);
            durationDistanceView.setTextColor(android.graphics.Color.WHITE);
            durationDistanceView.setGravity(android.view.Gravity.CENTER);
            priceCardLayout.addView(durationDistanceView);

            contentLayout.addView(priceCardLayout, priceCardParams);

            // LOCATION SECTION (matching React Native design with dots)
            android.widget.LinearLayout locationContainer = new android.widget.LinearLayout(this);
            locationContainer.setOrientation(android.widget.LinearLayout.VERTICAL);
            android.widget.LinearLayout.LayoutParams locationContainerParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            locationContainerParams.setMargins(0, 0, 0, 20);

            // Pickup location item
            android.widget.LinearLayout pickupItem = new android.widget.LinearLayout(this);
            pickupItem.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            pickupItem.setGravity(android.view.Gravity.CENTER_VERTICAL);
            android.widget.LinearLayout.LayoutParams pickupItemParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            pickupItemParams.setMargins(0, 0, 0, 16);

            // Pickup dot
            android.view.View pickupDot = new android.view.View(this);
            pickupDot.setBackgroundColor(android.graphics.Color.parseColor("#10B981"));
            android.widget.LinearLayout.LayoutParams pickupDotParams = new android.widget.LinearLayout.LayoutParams(12, 12);
            pickupDotParams.setMargins(0, 0, 12, 0);
            pickupItem.addView(pickupDot, pickupDotParams);

            // Pickup info
            android.widget.LinearLayout pickupInfo = new android.widget.LinearLayout(this);
            pickupInfo.setOrientation(android.widget.LinearLayout.VERTICAL);
            pickupInfo.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
            ));

            android.widget.TextView pickupTitleView = new android.widget.TextView(this);
            pickupTitleView.setText(popupPickupTitle);
            pickupTitleView.setTextSize(12);
            pickupTitleView.setTextColor(android.graphics.Color.parseColor("#10B981"));
            pickupTitleView.setTypeface(null, android.graphics.Typeface.BOLD);
            android.widget.LinearLayout.LayoutParams pickupTitleParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            pickupTitleParams.setMargins(0, 0, 0, 4);
            pickupInfo.addView(pickupTitleView, pickupTitleParams);

            android.widget.TextView pickupAddressView = new android.widget.TextView(this);
            pickupAddressView.setText(popupPickupAddress);
            pickupAddressView.setTextSize(14);
            pickupAddressView.setTextColor(android.graphics.Color.parseColor("#1F2937"));
            pickupInfo.addView(pickupAddressView);

            pickupItem.addView(pickupInfo);
            locationContainer.addView(pickupItem, pickupItemParams);

            // Destination location item
            android.widget.LinearLayout destinationItem = new android.widget.LinearLayout(this);
            destinationItem.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            destinationItem.setGravity(android.view.Gravity.CENTER_VERTICAL);

            // Destination dot
            android.view.View destinationDot = new android.view.View(this);
            destinationDot.setBackgroundColor(android.graphics.Color.parseColor("#EF4444"));
            android.widget.LinearLayout.LayoutParams destinationDotParams = new android.widget.LinearLayout.LayoutParams(12, 12);
            destinationDotParams.setMargins(0, 0, 12, 0);
            destinationItem.addView(destinationDot, destinationDotParams);

            // Destination info
            android.widget.LinearLayout destinationInfo = new android.widget.LinearLayout(this);
            destinationInfo.setOrientation(android.widget.LinearLayout.VERTICAL);
            destinationInfo.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
            ));

            android.widget.TextView destinationTitleView = new android.widget.TextView(this);
            destinationTitleView.setText(popupDestinationTitle);
            destinationTitleView.setTextSize(12);
            destinationTitleView.setTextColor(android.graphics.Color.parseColor("#EF4444"));
            destinationTitleView.setTypeface(null, android.graphics.Typeface.BOLD);
            android.widget.LinearLayout.LayoutParams destinationTitleParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            destinationTitleParams.setMargins(0, 0, 0, 4);
            destinationInfo.addView(destinationTitleView, destinationTitleParams);

            android.widget.TextView destinationAddressView = new android.widget.TextView(this);
            destinationAddressView.setText(popupDestinationAddress);
            destinationAddressView.setTextSize(14);
            destinationAddressView.setTextColor(android.graphics.Color.parseColor("#1F2937"));
            destinationInfo.addView(destinationAddressView);

            destinationItem.addView(destinationInfo);
            locationContainer.addView(destinationItem);

            contentLayout.addView(locationContainer, locationContainerParams);

            // PAYMENT SECTION
            android.widget.LinearLayout paymentContainer = new android.widget.LinearLayout(this);
            paymentContainer.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            paymentContainer.setGravity(android.view.Gravity.CENTER_VERTICAL);
            android.widget.LinearLayout.LayoutParams paymentContainerParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            paymentContainerParams.setMargins(0, 0, 0, 24);

            // Payment icon
            android.widget.TextView paymentIcon = new android.widget.TextView(this);
            paymentIcon.setText("💳");
            paymentIcon.setTextSize(20);
            android.widget.LinearLayout.LayoutParams paymentIconParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            paymentIconParams.setMargins(0, 0, 12, 0);
            paymentContainer.addView(paymentIcon, paymentIconParams);

            // Payment info
            android.widget.LinearLayout paymentInfo = new android.widget.LinearLayout(this);
            paymentInfo.setOrientation(android.widget.LinearLayout.VERTICAL);
            paymentInfo.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
                0, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
            ));

            android.widget.TextView paymentLabel = new android.widget.TextView(this);
            paymentLabel.setText("Metode Pembayaran");
            paymentLabel.setTextSize(12);
            paymentLabel.setTextColor(android.graphics.Color.parseColor("#6B7280"));
            paymentInfo.addView(paymentLabel);

            android.widget.TextView paymentType = new android.widget.TextView(this);
            paymentType.setText(popupPaymentMethod);
            paymentType.setTextSize(14);
            paymentType.setTextColor(android.graphics.Color.parseColor("#1F2937"));
            paymentType.setTypeface(null, android.graphics.Typeface.BOLD);
            paymentInfo.addView(paymentType);

            paymentContainer.addView(paymentInfo);
            contentLayout.addView(paymentContainer, paymentContainerParams);

            // BUTTON SECTION (matching React Native design)
            android.widget.LinearLayout buttonContainer = new android.widget.LinearLayout(this);
            buttonContainer.setOrientation(android.widget.LinearLayout.VERTICAL);

            // Accept button (green background)
            android.widget.Button acceptButton = new android.widget.Button(this);
            acceptButton.setText(popupAcceptText);
            acceptButton.setTextColor(android.graphics.Color.WHITE);
            acceptButton.setTextSize(16);
            acceptButton.setTypeface(null, android.graphics.Typeface.BOLD);
            
            // Create rounded background for accept button
            android.graphics.drawable.GradientDrawable acceptButtonBackground = new android.graphics.drawable.GradientDrawable();
            acceptButtonBackground.setColor(android.graphics.Color.parseColor("#10B981"));
            acceptButtonBackground.setCornerRadius(12);
            acceptButton.setBackground(acceptButtonBackground);
            
            android.widget.LinearLayout.LayoutParams acceptButtonParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            acceptButtonParams.setMargins(0, 0, 0, 12);
            acceptButton.setPadding(16, 16, 16, 16);
            
            acceptButton.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    // Add button press animation
                    v.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                                    .start();
                            }
                        })
                        .start();
                    
                    android.widget.Toast.makeText(getApplicationContext(), "Order accepted!", android.widget.Toast.LENGTH_SHORT).show();
                    animatePopupOut(popupLayout);
                }
            });

            // Reject button (gray background)
            android.widget.Button rejectButton = new android.widget.Button(this);
            rejectButton.setText(popupRejectText);
            rejectButton.setTextColor(android.graphics.Color.parseColor("#6B7280"));
            rejectButton.setTextSize(16);
            rejectButton.setTypeface(null, android.graphics.Typeface.BOLD);
            
            // Create rounded background for reject button
            android.graphics.drawable.GradientDrawable rejectButtonBackground = new android.graphics.drawable.GradientDrawable();
            rejectButtonBackground.setColor(android.graphics.Color.parseColor("#F9FAFB"));
            rejectButtonBackground.setCornerRadius(12);
            rejectButton.setBackground(rejectButtonBackground);
            
            android.widget.LinearLayout.LayoutParams rejectButtonParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rejectButton.setPadding(12, 12, 12, 12);
            
            rejectButton.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    // Add button press animation
                    v.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                                    .start();
                            }
                        })
                        .start();
                    
                    android.widget.Toast.makeText(getApplicationContext(), "Order rejected!", android.widget.Toast.LENGTH_SHORT).show();
                    animatePopupOut(popupLayout);
                }
            });

            buttonContainer.addView(acceptButton, acceptButtonParams);
            buttonContainer.addView(rejectButton, rejectButtonParams);
            contentLayout.addView(buttonContainer);

            // Add content to popup layout
            popupLayout.addView(contentLayout, contentParams);

            // Add click listener to close popup when tapping outside
            popupLayout.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    // Animate out before removing
                    animatePopupOut(popupLayout);
                }
            });

            // Add popup to window manager
            windowManager.addView(popupLayout, popupParams);
            
            // Animate popup in
            animatePopupIn(contentLayout);
            
            android.util.Log.d("FloatingBubble", "Popup overlay added successfully with new design matching React Native");

        } catch (Exception e) {
            android.util.Log.e("FloatingBubble", "Failed to show popup overlay: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void animatePopupIn(android.widget.LinearLayout contentLayout) {
        try {
            // Set initial state - invisible and scaled down
            contentLayout.setAlpha(0f);
            contentLayout.setScaleX(0.3f);
            contentLayout.setScaleY(0.3f);
            contentLayout.setTranslationY(50f);

            // Animate in with spring effect
            contentLayout.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(400)
                .setInterpolator(new android.view.animation.OvershootInterpolator(1.2f))
                .start();

            android.util.Log.d("FloatingBubble", "Popup animate in started");
        } catch (Exception e) {
            android.util.Log.e("FloatingBubble", "Failed to animate popup in: " + e.getMessage());
        }
    }

    private void animatePopupOut(android.widget.RelativeLayout popupLayout) {
        try {
            // Find content layout
            android.widget.LinearLayout contentLayout = null;
            for (int i = 0; i < popupLayout.getChildCount(); i++) {
                android.view.View child = popupLayout.getChildAt(i);
                if (child instanceof android.widget.LinearLayout) {
                    contentLayout = (android.widget.LinearLayout) child;
                    break;
                }
            }

            if (contentLayout != null) {
                // Animate out with fade and scale
                contentLayout.animate()
                    .alpha(0f)
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .translationY(-30f)
                    .setDuration(250)
                    .setInterpolator(new android.view.animation.AccelerateInterpolator())
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                windowManager.removeView(popupLayout);
                                android.util.Log.d("FloatingBubble", "Popup removed after animation");
                            } catch (Exception e) {
                                android.util.Log.e("FloatingBubble", "Failed to remove popup: " + e.getMessage());
                            }
                        }
                    })
                    .start();
            } else {
                // Fallback - remove immediately
                windowManager.removeView(popupLayout);
            }

            android.util.Log.d("FloatingBubble", "Popup animate out started");
        } catch (Exception e) {
            android.util.Log.e("FloatingBubble", "Failed to animate popup out: " + e.getMessage());
            // Fallback - remove immediately
            try {
                windowManager.removeView(popupLayout);
            } catch (Exception ex) {
                android.util.Log.e("FloatingBubble", "Failed to remove popup in fallback: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }
    }
}
