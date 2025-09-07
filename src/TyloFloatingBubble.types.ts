export interface TyloFloatingBubbleModuleEvents {
  onChange: { value: string };
  [key: string]: any; // Add index signature for EventsMap compatibility
}

export interface FloatingBubbleOptions {
  title?: string;
  subtitle?: string;
  orderData?: any;
  showBadge?: boolean;
  badgeCount?: number;
  autoRequestPermission?: boolean;
  icon?: string; // Path to custom icon image, defaults to tylo circle.png

  // Popup params
  popupTitle?: string;
  popupSubtitle?: string;
  popupPrice?: string;
  popupDuration?: string;
  popupDistance?: string;
  popupPickupTitle?: string;
  popupPickupAddress?: string;
  popupDestinationTitle?: string;
  popupDestinationAddress?: string;
  popupPaymentMethod?: string;
  popupAcceptText?: string;
  popupRejectText?: string;
}

import { ViewStyle } from 'react-native';

export interface TyloFloatingBubbleViewProps {
  url?: string;
  onLoad?: () => void;
  style?: ViewStyle;
}