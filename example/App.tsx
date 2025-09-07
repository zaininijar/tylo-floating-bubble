import React, { useState, useEffect } from "react";
import {
    NativeEventEmitter,
    NativeModules,
    ViewStyle,
    TextInput,
} from "react-native";
import TyloFloatingBubble, { FloatingBubbleHelper } from "tylo-floating-bubble";
import TyloFloatingBubblePopup from "../src/TyloFloatingBubblePopup";
import {
    Button,
    SafeAreaView,
    ScrollView,
    Text,
    View,
    Alert,
} from "react-native";

let useEventCompat: any;

try {
    // Coba pakai API baru (Expo 52+)
    const expo = require("expo");
    if (expo?.useEvent) {
        useEventCompat = expo.useEvent;
    }
} catch (e) {
    // Kalau ga ada, fallback
    useEventCompat = undefined;
}

export function useBubbleEvent(eventName: string) {
    const [eventData, setEventData] = useState<any>(null);

    useEffect(() => {
        if (useEventCompat) {
            // Expo SDK 52+
            return useEventCompat(NativeModules.TyloFloatingBubble, eventName);
        } else {
            // Expo SDK 49 fallback → NativeEventEmitter
            const emitter = new NativeEventEmitter(
                NativeModules.TyloFloatingBubble
            );
            const sub = emitter.addListener(eventName, setEventData);
            return () => sub.remove();
        }
    }, [eventName]);

    return eventData;
}

export default function App() {
    const [permissionStatus, setPermissionStatus] = useState<string>("Unknown");
    const [bubbleVisible, setBubbleVisible] = useState<boolean>(false);
    const [popupVisible, setPopupVisible] = useState<boolean>(false);
    const onChangePayload = useBubbleEvent("onChange");
    const onBubbleClick = useBubbleEvent("onBubbleClick");

    // Dynamic form states
    const [badgeCount, setBadgeCount] = useState<string>("1");
    const [popupTitle, setPopupTitle] = useState<string>("New Ride Request");
    const [popupPrice, setPopupPrice] = useState<string>("$24.50");
    const [popupDuration, setPopupDuration] = useState<string>("15 min");
    const [popupDistance, setPopupDistance] = useState<string>("8.2 km");
    const [popupPickupAddress, setPopupPickupAddress] = useState<string>(
        "123 Main Street, Downtown Area"
    );
    const [popupDestinationAddress, setPopupDestinationAddress] =
        useState<string>("456 Business Center, Tech District");

    // Initialize module when app starts
    useEffect(() => {
        // Initialize the module to set up event emitter
        if (
            NativeModules.TyloFloatingBubble &&
            NativeModules.TyloFloatingBubble.initialize
        ) {
            try {
                NativeModules.TyloFloatingBubble.initialize();
                console.log("Module initialized successfully");
            } catch (error) {
                console.log("Module initialization failed:", error);
            }
        }
    }, []);

    // Handle bubble click event
    useEffect(() => {
        console.log("onBubbleClick event data:", onBubbleClick);
        if (onBubbleClick) {
            console.log("Bubble clicked! Showing popup...");
            setPopupVisible(true);
        }
    }, [onBubbleClick]);

    // Debug popup state
    useEffect(() => {
        console.log("Popup visible state changed:", popupVisible);
    }, [popupVisible]);

    const checkPermission = async () => {
        try {
            const hasPermission = await FloatingBubbleHelper.checkPermission();
            setPermissionStatus(hasPermission ? "Granted" : "Denied");
        } catch (error) {
            console.error("Error checking permission:", error);
            setPermissionStatus("Error");
        }
    };

    const requestPermission = async () => {
        try {
            const granted = await FloatingBubbleHelper.requestPermission();
            setPermissionStatus(granted ? "Granted" : "Denied");
            if (!granted) {
                Alert.alert(
                    "Permission Required",
                    "Please grant overlay permission to use floating bubble"
                );
            }
        } catch (error) {
            console.error("Error requesting permission:", error);
            setPermissionStatus("Error");
        }
    };

    const showBubble = async () => {
        try {
            const bubbleData = {
                // Bubble params
                title: "New Order #1234",
                subtitle: "Tap to view order details",
                showBadge: true,
                badgeCount: parseInt(badgeCount) || 1,
                autoRequestPermission: true,

                // Popup params - using dynamic values
                popupTitle: popupTitle,
                popupSubtitle: "Tap to view details",
                popupPrice: popupPrice,
                popupDuration: popupDuration,
                popupDistance: popupDistance,
                popupPickupTitle: "Lokasi Penjemputan",
                popupPickupAddress: popupPickupAddress,
                popupDestinationTitle: "Tujuan",
                popupDestinationAddress: popupDestinationAddress,
                popupPaymentMethod: "Credit Card",
                popupAcceptText: "Terima Perjalanan",
                popupRejectText: "Tolak",
            };

            console.log("React Native - Sending bubble data:", bubbleData);
            console.log("React Native - Popup Title:", bubbleData.popupTitle);
            console.log("React Native - Popup Price:", bubbleData.popupPrice);
            console.log(
                "React Native - Popup Duration:",
                bubbleData.popupDuration
            );
            console.log(
                "React Native - Popup Distance:",
                bubbleData.popupDistance
            );
            console.log(
                "React Native - Popup Pickup:",
                bubbleData.popupPickupAddress
            );
            console.log(
                "React Native - Popup Destination:",
                bubbleData.popupDestinationAddress
            );

            const success = await FloatingBubbleHelper.showBubble(bubbleData);

            if (success) {
                setBubbleVisible(true);
                Alert.alert("Success", "Floating bubble shown!");
            } else {
                Alert.alert("Error", "Failed to show floating bubble");
            }
        } catch (error) {
            console.error("Error showing bubble:", error);
            Alert.alert("Error", "Failed to show floating bubble");
        }
    };

    const hideBubble = async () => {
        try {
            const success = await FloatingBubbleHelper.hideBubble();
            if (success) {
                setBubbleVisible(false);
                Alert.alert("Success", "Floating bubble hidden!");
            } else {
                Alert.alert("Error", "Failed to hide floating bubble");
            }
        } catch (error) {
            console.error("Error hiding bubble:", error);
            Alert.alert("Error", "Failed to hide floating bubble");
        }
    };

    const checkVisibility = async () => {
        try {
            const visible = await FloatingBubbleHelper.isVisible();
            setBubbleVisible(visible);
            Alert.alert(
                "Bubble Status",
                visible ? "Bubble is visible" : "Bubble is hidden"
            );
        } catch (error) {
            console.error("Error checking visibility:", error);
        }
    };

    return (
        <SafeAreaView style={styles.container}>
            <ScrollView style={styles.container}>
                <Text style={styles.header}>Tylo Floating Bubble Example</Text>

                <Group name="Permission Management">
                    <Text style={styles.status}>
                        Permission Status: {permissionStatus}
                    </Text>
                    <View style={styles.buttonContainer as ViewStyle}>
                        <Button
                            title="Check Permission"
                            onPress={checkPermission}
                        />
                        <Button
                            title="Request Permission"
                            onPress={requestPermission}
                        />
                    </View>
                </Group>

                <Group name="Bubble Controls">
                    <Text style={styles.status}>
                        Bubble Visible: {bubbleVisible ? "Yes" : "No"}
                    </Text>
                    <View style={styles.buttonContainer as ViewStyle}>
                        <Button title="Show Bubble" onPress={showBubble} />
                        <Button title="Hide Bubble" onPress={hideBubble} />
                        <Button
                            title="Check Visibility"
                            onPress={checkVisibility}
                        />
                    </View>
                </Group>

                <Group name="Badge Settings">
                    <Text style={styles.label}>Badge Count:</Text>
                    <TextInput
                        style={styles.input}
                        value={badgeCount}
                        onChangeText={setBadgeCount}
                        placeholder="Enter badge count"
                        keyboardType="numeric"
                    />
                </Group>

                <Group name="Popup Content Settings">
                    <Text style={styles.label}>Popup Title:</Text>
                    <TextInput
                        style={styles.input}
                        value={popupTitle}
                        onChangeText={setPopupTitle}
                        placeholder="Enter popup title"
                    />

                    <Text style={styles.label}>Price:</Text>
                    <TextInput
                        style={styles.input}
                        value={popupPrice}
                        onChangeText={setPopupPrice}
                        placeholder="Enter price (e.g., $24.50)"
                    />

                    <Text style={styles.label}>Duration:</Text>
                    <TextInput
                        style={styles.input}
                        value={popupDuration}
                        onChangeText={setPopupDuration}
                        placeholder="Enter duration (e.g., 15 min)"
                    />

                    <Text style={styles.label}>Distance:</Text>
                    <TextInput
                        style={styles.input}
                        value={popupDistance}
                        onChangeText={setPopupDistance}
                        placeholder="Enter distance (e.g., 8.2 km)"
                    />

                    <Text style={styles.label}>Pickup Address:</Text>
                    <TextInput
                        style={styles.input}
                        value={popupPickupAddress}
                        onChangeText={setPopupPickupAddress}
                        placeholder="Enter pickup address"
                        multiline
                    />

                    <Text style={styles.label}>Destination Address:</Text>
                    <TextInput
                        style={styles.input}
                        value={popupDestinationAddress}
                        onChangeText={setPopupDestinationAddress}
                        placeholder="Enter destination address"
                        multiline
                    />
                </Group>

                <Group name="Events">
                    <Text style={styles.eventText}>
                        Last Event: {onChangePayload?.value || "None"}
                    </Text>
                </Group>

                <Group name="WebView Component">
                    <Text style={styles.placeholderText}>
                        WebView Component (ViewManager removed for Expo 49
                        compatibility)
                    </Text>
                </Group>

                <Group name="Debug Popup">
                    <Button
                        title="Test Popup (Manual)"
                        onPress={() => {
                            console.log("Manual popup trigger");
                            setPopupVisible(true);
                        }}
                    />
                    <Text style={styles.debugText}>
                        Popup State: {popupVisible ? "Visible" : "Hidden"}
                    </Text>
                    <Text style={styles.debugText}>
                        Event Data: {JSON.stringify(onBubbleClick)}
                    </Text>
                </Group>
            </ScrollView>

            {/* Floating Bubble Popup */}
            <TyloFloatingBubblePopup
                visible={popupVisible}
                onClose={() => setPopupVisible(false)}
                title={popupTitle}
                subtitle="Tap to view details"
                price={popupPrice}
                duration={popupDuration}
                distance={popupDistance}
                pickupTitle="Lokasi Penjemputan"
                pickupAddress={popupPickupAddress}
                destinationTitle="Tujuan"
                destinationAddress={popupDestinationAddress}
                paymentMethod="Credit Card"
                acceptButtonText="Terima Perjalanan"
                rejectButtonText="Tolak"
                onAccept={() => {
                    Alert.alert("Accepted", "Ride request accepted!");
                    setPopupVisible(false);
                }}
                onReject={() => {
                    Alert.alert("Rejected", "Ride request rejected!");
                    setPopupVisible(false);
                }}
            />
        </SafeAreaView>
    );
}

function Group(props: { name: string; children: React.ReactNode }) {
    return (
        <View style={styles.group}>
            <Text style={styles.groupHeader}>{props.name}</Text>
            {props.children}
        </View>
    );
}

const styles = {
    header: {
        fontSize: 30,
        margin: 20,
        textAlign: "center" as const,
        fontWeight: "bold" as const,
        color: "#333",
    },
    groupHeader: {
        fontSize: 20,
        marginBottom: 15,
        fontWeight: "600" as const,
        color: "#444",
    },
    group: {
        margin: 15,
        backgroundColor: "#fff",
        borderRadius: 12,
        padding: 20,
        shadowColor: "#000",
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        elevation: 3,
    },
    container: {
        flex: 1,
        backgroundColor: "#f5f5f5",
    },
    webview: {
        height: 200,
        borderRadius: 8,
        overflow: "hidden" as const,
    },
    buttonContainer: {
        flexDirection: "row" as const,
        justifyContent: "space-around",
        flexWrap: "wrap" as const,
        gap: 10,
    },
    status: {
        fontSize: 16,
        marginBottom: 15,
        padding: 10,
        backgroundColor: "#f0f0f0",
        borderRadius: 8,
        textAlign: "center" as const,
    },
    eventText: {
        fontSize: 16,
        padding: 15,
        backgroundColor: "#e8f4fd",
        borderRadius: 8,
        borderLeftWidth: 4,
        borderLeftColor: "#2196F3",
    },
    placeholderText: {
        fontSize: 14,
        padding: 20,
        backgroundColor: "#f9f9f9",
        borderRadius: 8,
        textAlign: "center" as const,
        color: "#666",
        fontStyle: "italic" as const,
    },
    debugText: {
        fontSize: 12,
        padding: 10,
        backgroundColor: "#e8f4f8",
        borderRadius: 4,
        marginVertical: 2,
        color: "#333",
    },
    label: {
        fontSize: 14,
        fontWeight: "600" as const,
        color: "#333",
        marginBottom: 5,
        marginTop: 10,
    },
    input: {
        borderWidth: 1,
        borderColor: "#ddd",
        borderRadius: 8,
        padding: 12,
        fontSize: 16,
        backgroundColor: "#fff",
        marginBottom: 5,
    },
};
