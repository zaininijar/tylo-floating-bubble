# Tylo Floating Bubble

A customizable floating bubble component for React Native and Expo applications. Perfect for creating floating action buttons, notification bubbles, or interactive overlay elements.

## Features

✨ **Fully Customizable** - All content can be configured via parameters
🎯 **Drag & Drop** - Smooth dragging with snap-to-edge functionality
🗑️ **Drag to Delete** - Intuitive drag-to-delete with visual feedback
🎨 **Rich UI** - Beautiful expanded view with animations
📱 **Cross Platform** - Works on both iOS and Android
🔒 **Smart Interactions** - Locked dragging when expanded
🌟 **Smooth Animations** - Fluid transitions and micro-interactions

## Installation

```bash
npm install tylo-floating-bubble
```

or

```bash
yarn add tylo-floating-bubble
```

## Usage

### JavaScript (CommonJS/Metro)

```javascript
import TyloFloatingBubble, { FloatingBubbleHelper } from 'tylo-floating-bubble';

// Simple show/hide
async function showBubble() {
  await FloatingBubbleHelper.showBubble({
    title: 'New Notification',
    subtitle: 'Tap to view details',
    showBadge: true,
    badgeCount: 1,
  });
}

async function hideBubble() {
  await FloatingBubbleHelper.hideBubble();
}
```

### Basic Implementation

```typescript
import { TyloFloatingBubble } from 'tylo-floating-bubble';

export default function App() {
  const handleShow = () => {
    TyloFloatingBubble.show({
      title: "New Notification",
      subtitle: "Tap to view details",
      price: "$25.00",
      duration: "10 min",
      distance: "2.5 km"
    });
  };

  const handleHide = () => {
    TyloFloatingBubble.hide();
  };

  return (
    <View style={styles.container}>
      <Button title="Show Bubble" onPress={handleShow} />
      <Button title="Hide Bubble" onPress={handleHide} />
    </View>
  );
}
```

### Advanced Configuration

```typescript
TyloFloatingBubble.show({
  // Basic bubble settings
  title: "New Ride Request",
  subtitle: "Tap to view details",
  showBadge: true,
  badgeCount: 3,
  icon: "custom_icon",
  
  // Expanded view content
  price: "$24.50",
  duration: "15 min",
  distance: "8.2 km",
  
  // Location information
  pickupTitle: "Pickup Location",
  pickupAddress: "123 Main Street, Downtown Area",
  destinationTitle: "Destination",
  destinationAddress: "456 Business Center, Tech District",
  
  // Payment and actions
  paymentMethod: "Credit Card",
  acceptButtonText: "Accept Ride",
  rejectButtonText: "Decline"
});
```

## API Reference

### Methods

#### `TyloFloatingBubble.show(options)`

Displays the floating bubble with the specified configuration.

**Parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `title` | `string` | `"New Ride Request"` | Main title text |
| `subtitle` | `string` | `"Tap to view details"` | Subtitle text |
| `showBadge` | `boolean` | `true` | Show/hide notification badge |
| `badgeCount` | `number` | `1` | Badge count number |
| `icon` | `string` | `"tylo_circle"` | Icon name from drawable resources |
| `price` | `string` | `"$24.50"` | Price display text |
| `duration` | `string` | `"15 min"` | Duration text |
| `distance` | `string` | `"8.2 km"` | Distance text |
| `pickupTitle` | `string` | `"Lokasi Penjemputan"` | Pickup location label |
| `pickupAddress` | `string` | `"123 Main Street, Downtown Area"` | Pickup address |
| `destinationTitle` | `string` | `"Tujuan"` | Destination label |
| `destinationAddress` | `string` | `"456 Business Center, Tech District"` | Destination address |
| `paymentMethod` | `string` | `"Credit Card"` | Payment method text |
| `acceptButtonText` | `string` | `"Terima Perjalanan"` | Accept button text |
| `rejectButtonText` | `string` | `"Tolak"` | Reject button text |

#### `TyloFloatingBubble.hide()`

Hides the floating bubble.

### Bubble Behavior

- **Dragging**: Bubble can be dragged around the screen
- **Snap to Edge**: Automatically snaps to screen edges when released
- **Drag to Delete**: Drag bubble to bottom delete zone to remove
- **Expand/Collapse**: Tap to expand detailed view, tap again to collapse
- **Locked Interaction**: Dragging is disabled when expanded view is open
- **Smooth Animations**: All interactions include fluid animations

## Customization

### Icons

Place your custom icons in the appropriate drawable folders:

**Android:**

```
android/app/src/main/res/drawable/your_icon.png
```

**iOS:**
Add icons to your iOS project's bundle.

### Styling

The component uses a modern design with:

- Rounded corners and shadows
- Smooth animations and transitions
- Responsive layout for different screen sizes
- Material Design principles

## Examples

### Ride Sharing App

```typescript
TyloFloatingBubble.show({
  title: "New Ride Request",
  subtitle: "Passenger nearby",
  price: "$18.50",
  duration: "12 min",
  distance: "5.2 km",
  pickupAddress: "Central Park, NYC",
  destinationAddress: "Times Square, NYC",
  acceptButtonText: "Accept Ride",
  rejectButtonText: "Decline"
});
```

### Food Delivery

```typescript
TyloFloatingBubble.show({
  title: "New Order",
  subtitle: "Ready for pickup",
  price: "$32.75",
  duration: "8 min",
  distance: "1.8 km",
  pickupTitle: "Restaurant",
  pickupAddress: "Pizza Palace, Main St",
  destinationTitle: "Customer",
  destinationAddress: "123 Oak Avenue",
  acceptButtonText: "Accept Order",
  rejectButtonText: "Skip"
});
```

### Simple Notification

```typescript
TyloFloatingBubble.show({
  title: "New Message",
  subtitle: "You have 3 unread messages",
  showBadge: true,
  badgeCount: 3,
  acceptButtonText: "View Messages",
  rejectButtonText: "Later"
});
```

## Requirements

- React Native 0.64+
- Expo SDK 45+
- iOS 11.0+
- Android API 21+

## Permissions

### Android

Add to your `android/app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```

### iOS

No additional permissions required.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

MIT License - see the [LICENSE](LICENSE) file for details.

## Support

If you like this project, please consider giving it a ⭐️ on [GitHub](https://github.com/zaininijar/tylo-floating-bubble)!

For issues and feature requests, please use the [GitHub Issues](https://github.com/zaininijar/tylo-floating-bubble/issues) page.

---

Made with ❤️ by [zaininijar](https://github.com/zaininijar)
