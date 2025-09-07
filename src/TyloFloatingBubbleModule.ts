import { requireNativeModule } from 'expo-modules-core';

import { TyloFloatingBubbleModuleEvents } from './TyloFloatingBubble.types';

declare class TyloFloatingBubbleModule {
  showBubble(data: any): Promise<boolean>;
  hideBubble(): Promise<boolean>;
  checkBubblePermission(): Promise<boolean>;
  requestBubblePermission(): Promise<boolean>;
  isBubbleVisible(): Promise<boolean>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<TyloFloatingBubbleModule>('TyloFloatingBubble');