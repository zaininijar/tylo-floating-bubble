# Tylo Floating Bubble

A React Native floating bubble component with native Android overlay popup for Expo SDK 49+.

## Features

- 🎈 **Floating Bubble**: Draggable floating bubble with snap-to-edge functionality
- 🎨 **Modern UI**: Beautiful popup with animations and modern design
- 📱 **Native Android**: Built with native Android overlay for smooth performance
- ⚡ **Expo Compatible**: Works with Expo SDK 49+
- 🎯 **Customizable**: Dynamic content and styling options
- 🔄 **Smooth Animations**: Spring animations and smooth transitions

## Installation

```bash
npm install tylo-floating-bubble
```

## Usage

### Basic Usage

```tsx
import React from 'react';
import { FloatingBubbleHelper } from 'tylo-floating-bubble';

const App = () => {
  const showBubble = async () => {
    const bubbleData = {
      // Bubble params
      title: "New Order #1234",
      subtitle: "Tap to view order details",
      showBadge: true,
      badgeCount: 1,
      
      // Popup params
      popupTitle: "New Ride Request",
      popupPrice: "$24.50",
      popupDuration: "15 min",
      popupDistance: "8.2 km",
      popupPickupAddress: "123 Main Street, Downtown Area",
      popupDestinationAddress: "456 Business Center, Tech District",
      popupPaymentMethod: "Credit Card",
      popupAcceptText: "Accept",
      popupRejectText: "Reject",
    };

    const success = await FloatingBubbleHelper.showBubble(bubbleData);
    if (success) {
      console.log("Bubble shown successfully!");
    }
  };

  return (
    <View>
      <Button title="Show Bubble" onPress={showBubble} />
    </View>
  );
};
```

### Permission Management

```tsx
import { FloatingBubbleHelper } from 'tylo-floating-bubble';

// Check permission
const hasPermission = await FloatingBubbleHelper.checkPermission();

// Request permission
const granted = await FloatingBubbleHelper.requestPermission();
```

### Bubble Controls

```tsx
// Show bubble
await FloatingBubbleHelper.showBubble(bubbleData);

// Hide bubble
await FloatingBubbleHelper.hideBubble();

// Check if bubble is visible
const isVisible = await FloatingBubbleHelper.isVisible();
```

## API Reference

### FloatingBubbleHelper

#### `showBubble(options: FloatingBubbleOptions): Promise<boolean>`

Shows the floating bubble with the specified options.

**Options:**
- `title?: string` - Bubble title
- `subtitle?: string` - Bubble subtitle
- `showBadge?: boolean` - Show badge on bubble
- `badgeCount?: number` - Badge count number
- `popupTitle?: string` - Popup title
- `popupPrice?: string` - Popup price
- `popupDuration?: string` - Popup duration
- `popupDistance?: string` - Popup distance
- `popupPickupAddress?: string` - Pickup address
- `popupDestinationAddress?: string` - Destination address
- `popupPaymentMethod?: string` - Payment method
- `popupAcceptText?: string` - Accept button text
- `popupRejectText?: string` - Reject button text

#### `hideBubble(): Promise<boolean>`

Hides the floating bubble.

#### `isVisible(): Promise<boolean>`

Checks if the bubble is currently visible.

#### `checkPermission(): Promise<boolean>`

Checks if overlay permission is granted.

#### `requestPermission(): Promise<boolean>`

Requests overlay permission from the user.

## Android Permissions

The library requires the following permission in your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

## Features

### 🎈 Floating Bubble
- Draggable bubble that snaps to screen edges
- Customizable badge with count
- Smooth animations and transitions

### 🎨 Popup Design
- Modern UI with rounded corners and shadows
- Green price card with white text
- Location dots (green for pickup, red for destination)
- Payment method display with emoji
- Accept/Reject buttons with animations

### ⚡ Animations
- Spring animation for popup appearance
- Scale animation for button presses
- Smooth fade out when closing
- Bubble click feedback animation

## Requirements

- React Native 0.72+
- Expo SDK 49+
- Android API 21+ (Android 5.0+)

## Example

Check out the `example/` directory for a complete working example.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

MIT © [zaininijar](https://github.com/zaininijar)

## Support

If you find this library helpful, please give it a ⭐ on GitHub!