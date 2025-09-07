import { TyloFloatingBubbleModuleEvents } from './TyloFloatingBubble.types';

// Mock implementation for web platform
class TyloFloatingBubbleModule {
  async showBubble(_data: any): Promise<boolean> {
    console.warn('TyloFloatingBubble is not supported on web');
    return false;
  }

  async hideBubble(): Promise<boolean> {
    console.warn('TyloFloatingBubble is not supported on web');
    return false;
  }

  async checkBubblePermission(): Promise<boolean> {
    console.warn('TyloFloatingBubble is not supported on web');
    return false;
  }

  async requestBubblePermission(): Promise<boolean> {
    console.warn('TyloFloatingBubble is not supported on web');
    return false;
  }

  async isBubbleVisible(): Promise<boolean> {
    console.warn('TyloFloatingBubble is not supported on web');
    return false;
  }
}

export default new TyloFloatingBubbleModule();