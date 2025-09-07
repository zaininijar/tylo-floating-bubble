import React, { useEffect, useState } from "react";
import {
    View,
    Text,
    StyleSheet,
    Modal,
    TouchableOpacity,
    Dimensions,
    Animated,
    PanResponder,
} from "react-native";

interface TyloFloatingBubblePopupProps {
    visible: boolean;
    onClose: () => void;
    title?: string;
    subtitle?: string;
    price?: string;
    duration?: string;
    distance?: string;
    pickupTitle?: string;
    pickupAddress?: string;
    destinationTitle?: string;
    destinationAddress?: string;
    paymentMethod?: string;
    acceptButtonText?: string;
    rejectButtonText?: string;
    onAccept?: () => void;
    onReject?: () => void;
}

const { width: screenWidth, height: screenHeight } = Dimensions.get("window");

export default function TyloFloatingBubblePopup({
    visible,
    onClose,
    title = "New Order",
    subtitle = "Tap to view details",
    price = "$24.50",
    duration = "15 min",
    distance = "8.2 km",
    pickupTitle = "Lokasi Penjemputan",
    pickupAddress = "123 Main Street, Downtown Area",
    destinationTitle = "Tujuan",
    destinationAddress = "456 Business Center, Tech District",
    paymentMethod = "Credit Card",
    acceptButtonText = "Terima Perjalanan",
    rejectButtonText = "Tolak",
    onAccept,
    onReject,
}: TyloFloatingBubblePopupProps) {
    const [fadeAnim] = useState(new Animated.Value(0));
    const [scaleAnim] = useState(new Animated.Value(0.3));
    const [translateY] = useState(new Animated.Value(50));

    useEffect(() => {
        if (visible) {
            // Show animation
            Animated.parallel([
                Animated.timing(fadeAnim, {
                    toValue: 1,
                    duration: 300,
                    useNativeDriver: true,
                }),
                Animated.spring(scaleAnim, {
                    toValue: 1,
                    tension: 50,
                    friction: 7,
                    useNativeDriver: true,
                }),
                Animated.timing(translateY, {
                    toValue: 0,
                    duration: 300,
                    useNativeDriver: true,
                }),
            ]).start();
        } else {
            // Hide animation
            Animated.parallel([
                Animated.timing(fadeAnim, {
                    toValue: 0,
                    duration: 200,
                    useNativeDriver: true,
                }),
                Animated.timing(scaleAnim, {
                    toValue: 0.3,
                    duration: 200,
                    useNativeDriver: true,
                }),
                Animated.timing(translateY, {
                    toValue: 50,
                    duration: 200,
                    useNativeDriver: true,
                }),
            ]).start();
        }
    }, [visible]);

    const handleAccept = () => {
        onAccept?.();
        onClose();
    };

    const handleReject = () => {
        onReject?.();
        onClose();
    };

    return (
        <Modal
            visible={visible}
            transparent
            animationType="none"
            onRequestClose={onClose}
        >
            <Animated.View style={[styles.overlay, { opacity: fadeAnim }]}>
                <TouchableOpacity
                    style={styles.overlayTouchable}
                    activeOpacity={1}
                    onPress={onClose}
                >
                    <Animated.View
                        style={[
                            styles.popup,
                            {
                                transform: [
                                    { scale: scaleAnim },
                                    { translateY: translateY },
                                ],
                            },
                        ]}
                    >
                        <TouchableOpacity activeOpacity={1}>
                            {/* Header */}
                            <View style={styles.header}>
                                <Text style={styles.title}>{title}</Text>
                                <Text style={styles.subtitle}>{subtitle}</Text>
                            </View>

                            {/* Price Card */}
                            <View style={styles.priceCard}>
                                <Text style={styles.priceText}>{price}</Text>
                                <Text style={styles.timeDistanceText}>
                                    ⏱ {duration} • {distance}
                                </Text>
                            </View>

                            {/* Location Info */}
                            <View style={styles.locationContainer}>
                                {/* Pickup */}
                                <View style={styles.locationItem}>
                                    <View style={styles.locationDot} />
                                    <View style={styles.locationInfo}>
                                        <Text style={styles.locationTitle}>
                                            {pickupTitle}
                                        </Text>
                                        <Text style={styles.locationAddress}>
                                            {pickupAddress}
                                        </Text>
                                    </View>
                                </View>

                                {/* Destination */}
                                <View style={styles.locationItem}>
                                    <View
                                        style={[
                                            styles.locationDot,
                                            styles.destinationDot,
                                        ]}
                                    />
                                    <View style={styles.locationInfo}>
                                        <Text
                                            style={[
                                                styles.locationTitle,
                                                styles.destinationTitle,
                                            ]}
                                        >
                                            {destinationTitle}
                                        </Text>
                                        <Text style={styles.locationAddress}>
                                            {destinationAddress}
                                        </Text>
                                    </View>
                                </View>
                            </View>

                            {/* Payment Method */}
                            <View style={styles.paymentContainer}>
                                <Text style={styles.paymentIcon}>💳</Text>
                                <View style={styles.paymentInfo}>
                                    <Text style={styles.paymentLabel}>
                                        Metode Pembayaran
                                    </Text>
                                    <Text style={styles.paymentType}>
                                        {paymentMethod}
                                    </Text>
                                </View>
                            </View>

                            {/* Action Buttons */}
                            <View style={styles.buttonContainer}>
                                <TouchableOpacity
                                    style={styles.acceptButton}
                                    onPress={handleAccept}
                                >
                                    <Text style={styles.acceptButtonText}>
                                        {acceptButtonText}
                                    </Text>
                                </TouchableOpacity>

                                <TouchableOpacity
                                    style={styles.rejectButton}
                                    onPress={handleReject}
                                >
                                    <Text style={styles.rejectButtonText}>
                                        {rejectButtonText}
                                    </Text>
                                </TouchableOpacity>
                            </View>
                        </TouchableOpacity>
                    </Animated.View>
                </TouchableOpacity>
            </Animated.View>
        </Modal>
    );
}

const styles = StyleSheet.create({
    overlay: {
        flex: 1,
        backgroundColor: "rgba(0, 0, 0, 0.5)",
        justifyContent: "center",
        alignItems: "center",
    },
    overlayTouchable: {
        flex: 1,
        width: "100%",
        justifyContent: "center",
        alignItems: "center",
    },
    popup: {
        width: screenWidth * 0.9,
        backgroundColor: "white",
        borderRadius: 24,
        padding: 24,
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: 4,
        },
        shadowOpacity: 0.25,
        shadowRadius: 12,
        elevation: 12,
    },
    header: {
        alignItems: "center",
        marginBottom: 20,
    },
    title: {
        fontSize: 20,
        fontWeight: "bold",
        color: "#1F2937",
        marginBottom: 8,
    },
    subtitle: {
        fontSize: 14,
        color: "#6B7280",
    },
    priceCard: {
        backgroundColor: "#10B981",
        borderRadius: 16,
        padding: 24,
        alignItems: "center",
        marginBottom: 20,
    },
    priceText: {
        fontSize: 28,
        fontWeight: "bold",
        color: "white",
    },
    timeDistanceText: {
        fontSize: 14,
        color: "white",
        marginTop: 8,
    },
    locationContainer: {
        marginBottom: 20,
    },
    locationItem: {
        flexDirection: "row",
        alignItems: "flex-start",
        marginBottom: 16,
    },
    locationDot: {
        width: 12,
        height: 12,
        borderRadius: 6,
        backgroundColor: "#10B981",
        marginRight: 12,
        marginTop: 4,
    },
    destinationDot: {
        backgroundColor: "#EF4444",
    },
    locationInfo: {
        flex: 1,
    },
    locationTitle: {
        fontSize: 12,
        fontWeight: "bold",
        color: "#10B981",
        marginBottom: 4,
    },
    destinationTitle: {
        color: "#EF4444",
    },
    locationAddress: {
        fontSize: 14,
        color: "#1F2937",
        lineHeight: 20,
    },
    paymentContainer: {
        flexDirection: "row",
        alignItems: "center",
        marginBottom: 24,
    },
    paymentIcon: {
        fontSize: 20,
        marginRight: 12,
    },
    paymentInfo: {
        flex: 1,
    },
    paymentLabel: {
        fontSize: 12,
        color: "#6B7280",
    },
    paymentType: {
        fontSize: 14,
        fontWeight: "bold",
        color: "#1F2937",
    },
    buttonContainer: {
        gap: 12,
    },
    acceptButton: {
        backgroundColor: "#10B981",
        borderRadius: 12,
        padding: 16,
        alignItems: "center",
    },
    acceptButtonText: {
        color: "white",
        fontSize: 16,
        fontWeight: "bold",
    },
    rejectButton: {
        backgroundColor: "#F9FAFB",
        borderRadius: 12,
        padding: 12,
        alignItems: "center",
    },
    rejectButtonText: {
        color: "#6B7280",
        fontSize: 16,
        fontWeight: "bold",
    },
});
