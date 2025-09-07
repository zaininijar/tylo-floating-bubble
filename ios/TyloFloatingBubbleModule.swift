import ExpoModulesCore
import UIKit

public class TyloFloatingBubbleModule: Module {
  private var floatingWindow: UIWindow?
  private var floatingViewController: FloatingBubbleViewController?
  private var isVisible = false
  
  public func definition() -> ModuleDefinition {
    Name("TyloFloatingBubble")

    // Defines event names that the module can send to JavaScript.
    Events("onChange")

    // Check bubble permission (always true on iOS as no special permission needed)
    AsyncFunction("checkBubblePermission") {
      return true
    }

    // Request bubble permission (always true on iOS as no special permission needed)
    AsyncFunction("requestBubblePermission") {
      return true
    }

    // Show floating bubble
    AsyncFunction("showBubble") { (data: [String: Any]) -> Bool in
      DispatchQueue.main.async {
        self.showFloatingBubble(data: data)
      }
      return true
    }

    // Hide floating bubble
    AsyncFunction("hideBubble") { () -> Bool in
      DispatchQueue.main.async {
        self.hideFloatingBubble()
      }
      return true
    }

    // Check if bubble is visible
    AsyncFunction("isBubbleVisible") {
      return self.isVisible
    }

    // Enables the module to be used as a native view.
    View(TyloFloatingBubbleView.self) {
      Prop("url") { (view: TyloFloatingBubbleView, url: URL) in
        if view.webView.url != url {
          view.webView.load(URLRequest(url: url))
        }
      }
      Events("onLoad")
    }
  }
  
  private func showFloatingBubble(data: [String: Any]) {
    guard floatingWindow == nil else { return }
    
    let title = data["title"] as? String ?? "New Order"
    let subtitle = data["subtitle"] as? String ?? "Tap to view details"
    let showBadge = data["showBadge"] as? Bool ?? true
    let badgeCount = data["badgeCount"] as? Int ?? 1
    let iconName = data["icon"] as? String ?? "tylo_circle"
    
    // Create floating window
    if #available(iOS 13.0, *) {
      if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
        floatingWindow = UIWindow(windowScene: windowScene)
      }
    } else {
      floatingWindow = UIWindow(frame: UIScreen.main.bounds)
    }
    
    guard let window = floatingWindow else { return }
    
    window.windowLevel = UIWindow.Level.alert + 1
    window.backgroundColor = UIColor.clear
    window.isHidden = false
    
    // Create floating bubble view controller
    floatingViewController = FloatingBubbleViewController(
      title: title,
      subtitle: subtitle,
      showBadge: showBadge,
      badgeCount: badgeCount,
      iconName: iconName
    )
    
    window.rootViewController = floatingViewController
    window.makeKeyAndVisible()
    
    isVisible = true
    
    // Send event to JavaScript
    sendEvent("onChange", [
      "value": "Bubble shown"
    ])
  }
  
  private func hideFloatingBubble() {
    floatingWindow?.isHidden = true
    floatingWindow = nil
    floatingViewController = nil
    isVisible = false
    
    // Send event to JavaScript
    sendEvent("onChange", [
      "value": "Bubble hidden"
    ])
  }
}
