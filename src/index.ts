// Reexport the native module. On web, it will be resolved to TyloFloatingBubbleModule.web.ts
// and on native platforms to TyloFloatingBubbleModule.ts
export { default } from './TyloFloatingBubbleModule';
// TyloFloatingBubbleView removed for Expo 49 compatibility
export { default as TyloFloatingBubblePopup } from './TyloFloatingBubblePopup';
export * from './TyloFloatingBubble.types';

// Helper functions for the floating bubble
export const FloatingBubbleHelper = {
    /**
     * Show the floating bubble
     * @param {Object} options - Options for the bubble
     * @param {string} options.title - Title to display in the bubble detail (default: 'New Order')
     * @param {string} options.subtitle - Subtitle to display in the bubble detail (default: 'Tap to view details')
     * @param {Object} options.orderData - Order data to pass to the bubble
     * @param {boolean} options.showBadge - Show notification badge (default: true if orderData is provided)
     * @param {number} options.badgeCount - Number to display in the badge (default: 1)
     * @param {boolean} options.autoRequestPermission - Automatically request permission if needed (default: false)
     * @param {string} options.icon - Path to custom icon image (default: uses tylo circle.png)
     * @returns {Promise<boolean>} - Whether the bubble was shown successfully
     */
    async showBubble(options: {
        title?: string;
        subtitle?: string;
        orderData?: any;
        showBadge?: boolean;
        badgeCount?: number;
        autoRequestPermission?: boolean;
        icon?: string;
    } = {}): Promise<boolean> {
        const TyloFloatingBubble = require('./TyloFloatingBubbleModule').default;

        // Check permission first
        const hasPermission = await TyloFloatingBubble.checkBubblePermission();

        // Request permission if needed and auto-request is enabled
        if (!hasPermission && options.autoRequestPermission) {
            await TyloFloatingBubble.requestBubblePermission();
            // We need to check again after requesting
            if (!await TyloFloatingBubble.checkBubblePermission()) {
                console.error('Floating bubble permission denied');
                return false;
            }
        } else if (!hasPermission) {
            console.error('Floating bubble permission not granted');
            return false;
        }

        // Prepare bubble data
        const bubbleData = {
            title: options.title || 'New Order',
            subtitle: options.subtitle || 'Tap to view details',
            orderData: options.orderData || null,
            showBadge: options.showBadge !== undefined ? options.showBadge : !!options.orderData,
            badgeCount: options.badgeCount || 1,
            icon: options.icon || 'tylo_circle' // Default to tylo circle icon
        };

        // Show the bubble
        return await TyloFloatingBubble.showBubble(bubbleData);
    },

    /**
     * Hide the floating bubble
     * @returns {Promise<boolean>} - Whether the bubble was hidden successfully
     */
    async hideBubble(): Promise<boolean> {
        const TyloFloatingBubble = require('./TyloFloatingBubbleModule').default;
        return await TyloFloatingBubble.hideBubble();
    },

    /**
     * Check if the floating bubble permission is granted
     * @returns {Promise<boolean>} - Whether the permission is granted
     */
    async checkPermission(): Promise<boolean> {
        const TyloFloatingBubble = require('./TyloFloatingBubbleModule').default;
        console.log('checkPermission');
        return await TyloFloatingBubble.checkBubblePermission();
    },

    /**
     * Request the floating bubble permission
     * @returns {Promise<boolean>} - Whether the permission request was successful
     */
    async requestPermission(): Promise<boolean> {
        const TyloFloatingBubble = require('./TyloFloatingBubbleModule').default;
        console.log('requestPermission');
        return await TyloFloatingBubble.requestBubblePermission();
    },

    /**
     * Check if the floating bubble is visible
     * @returns {Promise<boolean>} - Whether the bubble is visible
     */
    async isVisible(): Promise<boolean> {
        const TyloFloatingBubble = require('./TyloFloatingBubbleModule').default;
        return await TyloFloatingBubble.isBubbleVisible();
    }
};