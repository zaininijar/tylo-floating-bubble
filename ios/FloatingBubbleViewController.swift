import UIKit

class FloatingBubbleViewController: UIViewController {
    private let titleText: String
    private let subtitleText: String
    private let showBadge: Bool
    private let badgeCount: Int
    private let iconName: String
    
    private var bubbleView: UIView!
    private var expandedView: UIView!
    private var isExpanded = false
    
    private var panGesture: UIPanGestureRecognizer!
    private var tapGesture: UITapGestureRecognizer!
    private var deleteArea: UIView!
    private var isInDeleteZone = false
    
    init(title: String, subtitle: String, showBadge: Bool, badgeCount: Int, iconName: String) {
        self.titleText = title
        self.subtitleText = subtitle
        self.showBadge = showBadge
        self.badgeCount = badgeCount
        self.iconName = iconName
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupFloatingBubble()
    }
    
    private func setupFloatingBubble() {
        view.backgroundColor = UIColor.clear
        
        // Create bubble view
        createBubbleView()
        
        // Create expanded view
        createExpandedView()
        
        // Add gestures
        setupGestures()
        
        // Position bubble in top-right corner initially
        positionBubble()
    }
    
    private func createBubbleView() {
        // Create simple bubble container - just for icon
        let bubbleContainer = UIView()
        bubbleContainer.translatesAutoresizingMaskIntoConstraints = false
        
        // Create main bubble view - no background, no effects
        bubbleView = UIView()
        bubbleView.translatesAutoresizingMaskIntoConstraints = false
        
        // Add icon image - simple and clean
        let iconImageView = UIImageView()
        iconImageView.contentMode = .scaleAspectFit
        iconImageView.translatesAutoresizingMaskIntoConstraints = false
        
        // Set icon from bundle
        if let image = UIImage(named: iconName) {
            iconImageView.image = image
        } else {
            // Fallback to default icon
            iconImageView.image = UIImage(named: "tylo_circle")
        }
        
        bubbleView.addSubview(iconImageView)
        
        // Create simple badge - positioned relative to icon
        if showBadge && badgeCount > 0 {
            let badgeLabel = UILabel()
            badgeLabel.text = badgeCount > 99 ? "99+" : "\(badgeCount)"
            badgeLabel.textColor = UIColor.white
            badgeLabel.font = UIFont.boldSystemFont(ofSize: 10)
            badgeLabel.textAlignment = .center
            badgeLabel.backgroundColor = UIColor(red: 1.0, green: 0.27, blue: 0.27, alpha: 1.0)
            badgeLabel.layer.cornerRadius = 9
            badgeLabel.clipsToBounds = true
            badgeLabel.translatesAutoresizingMaskIntoConstraints = false
            
            bubbleContainer.addSubview(badgeLabel)
            
            NSLayoutConstraint.activate([
                badgeLabel.topAnchor.constraint(equalTo: bubbleContainer.topAnchor, constant: 6),
                badgeLabel.trailingAnchor.constraint(equalTo: bubbleContainer.trailingAnchor, constant: -6),
                badgeLabel.widthAnchor.constraint(greaterThanOrEqualToConstant: 18),
                badgeLabel.heightAnchor.constraint(equalToConstant: 18)
            ])
        }
        
        bubbleContainer.addSubview(bubbleView)
        view.addSubview(bubbleContainer)
        
        NSLayoutConstraint.activate([
            bubbleContainer.widthAnchor.constraint(equalToConstant: 56),
            bubbleContainer.heightAnchor.constraint(equalToConstant: 56),
            
            bubbleView.centerXAnchor.constraint(equalTo: bubbleContainer.centerXAnchor),
            bubbleView.centerYAnchor.constraint(equalTo: bubbleContainer.centerYAnchor),
            bubbleView.widthAnchor.constraint(equalToConstant: 56),
            bubbleView.heightAnchor.constraint(equalToConstant: 56),
            
            iconImageView.centerXAnchor.constraint(equalTo: bubbleView.centerXAnchor),
            iconImageView.centerYAnchor.constraint(equalTo: bubbleView.centerYAnchor),
            iconImageView.widthAnchor.constraint(equalToConstant: 56),
            iconImageView.heightAnchor.constraint(equalToConstant: 56)
        ])
        
        // Store reference to container for gestures
        self.bubbleView = bubbleContainer
        
        // Create delete area
        createDeleteArea()
    }
    
    private func createDeleteArea() {
        deleteArea = UIView()
        deleteArea.backgroundColor = UIColor(red: 1.0, green: 0.27, blue: 0.27, alpha: 1.0)
        deleteArea.layer.cornerRadius = 40
        deleteArea.isHidden = true
        deleteArea.translatesAutoresizingMaskIntoConstraints = false
        
        let deleteLabel = UILabel()
        deleteLabel.text = "✕"
        deleteLabel.textColor = UIColor.white
        deleteLabel.font = UIFont.boldSystemFont(ofSize: 24)
        deleteLabel.textAlignment = .center
        deleteLabel.translatesAutoresizingMaskIntoConstraints = false
        
        deleteArea.addSubview(deleteLabel)
        view.addSubview(deleteArea)
        
        NSLayoutConstraint.activate([
            deleteArea.widthAnchor.constraint(equalToConstant: 80),
            deleteArea.heightAnchor.constraint(equalToConstant: 80),
            deleteArea.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            deleteArea.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -100),
            
            deleteLabel.centerXAnchor.constraint(equalTo: deleteArea.centerXAnchor),
            deleteLabel.centerYAnchor.constraint(equalTo: deleteArea.centerYAnchor)
        ])
    }
    
    private func createExpandedView() {
        expandedView = UIView()
        expandedView.layer.cornerRadius = 24
        expandedView.layer.shadowColor = UIColor.black.cgColor
        expandedView.layer.shadowOffset = CGSize(width: 0, height: 8)
        expandedView.layer.shadowOpacity = 0.3
        expandedView.layer.shadowRadius = 16
        expandedView.isHidden = true
        
        // Glass morphism background with gradient
        let gradientLayer = CAGradientLayer()
        gradientLayer.frame = CGRect(x: 0, y: 0, width: 240, height: 140)
        gradientLayer.cornerRadius = 24
        gradientLayer.colors = [
            UIColor(white: 1.0, alpha: 0.95).cgColor,
            UIColor(white: 1.0, alpha: 0.85).cgColor
        ]
        gradientLayer.borderWidth = 1
        gradientLayer.borderColor = UIColor(white: 1.0, alpha: 0.2).cgColor
        expandedView.layer.insertSublayer(gradientLayer, at: 0)
        
        // Header container with icon
        let headerContainer = UIView()
        headerContainer.translatesAutoresizingMaskIntoConstraints = false
        
        let iconLabel = UILabel()
        iconLabel.text = "🎉"
        iconLabel.font = UIFont.systemFont(ofSize: 20)
        iconLabel.translatesAutoresizingMaskIntoConstraints = false
        
        let titleLabel = UILabel()
        titleLabel.text = titleText
        titleLabel.font = UIFont.boldSystemFont(ofSize: 18)
        titleLabel.textColor = UIColor(red: 0.1, green: 0.1, blue: 0.1, alpha: 1.0)
        titleLabel.numberOfLines = 0
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        
        headerContainer.addSubview(iconLabel)
        headerContainer.addSubview(titleLabel)
        
        // Subtitle with better styling
        let subtitleLabel = UILabel()
        subtitleLabel.text = subtitleText
        subtitleLabel.font = UIFont.systemFont(ofSize: 14)
        subtitleLabel.textColor = UIColor(red: 0.4, green: 0.4, blue: 0.4, alpha: 1.0)
        subtitleLabel.numberOfLines = 0
        subtitleLabel.translatesAutoresizingMaskIntoConstraints = false
        
        // Button container
        let buttonContainer = UIView()
        buttonContainer.translatesAutoresizingMaskIntoConstraints = false
        
        // View Details button with gradient
        let viewButton = UIButton(type: .system)
        viewButton.setTitle("View Details", for: .normal)
        viewButton.setTitleColor(.white, for: .normal)
        viewButton.titleLabel?.font = UIFont.boldSystemFont(ofSize: 13)
        viewButton.layer.cornerRadius = 20
        viewButton.layer.shadowColor = UIColor.black.cgColor
        viewButton.layer.shadowOffset = CGSize(width: 0, height: 2)
        viewButton.layer.shadowOpacity = 0.3
        viewButton.layer.shadowRadius = 4
        viewButton.translatesAutoresizingMaskIntoConstraints = false
        
        let viewButtonGradient = CAGradientLayer()
        viewButtonGradient.frame = CGRect(x: 0, y: 0, width: 100, height: 40)
        viewButtonGradient.cornerRadius = 20
        viewButtonGradient.colors = [
            UIColor(red: 1.0, green: 0.42, blue: 0.21, alpha: 1.0).cgColor,
            UIColor(red: 0.97, green: 0.58, blue: 0.12, alpha: 1.0).cgColor
        ]
        viewButton.layer.insertSublayer(viewButtonGradient, at: 0)
        
        // Dismiss button
        let dismissButton = UIButton(type: .system)
        dismissButton.setTitle("Dismiss", for: .normal)
        dismissButton.setTitleColor(UIColor(red: 0.4, green: 0.4, blue: 0.4, alpha: 1.0), for: .normal)
        dismissButton.titleLabel?.font = UIFont.systemFont(ofSize: 13)
        dismissButton.backgroundColor = UIColor(red: 0.96, green: 0.96, blue: 0.96, alpha: 1.0)
        dismissButton.layer.cornerRadius = 20
        dismissButton.translatesAutoresizingMaskIntoConstraints = false
        
        buttonContainer.addSubview(viewButton)
        buttonContainer.addSubview(dismissButton)
        
        expandedView.addSubview(headerContainer)
        expandedView.addSubview(subtitleLabel)
        expandedView.addSubview(buttonContainer)
        view.addSubview(expandedView)
        expandedView.translatesAutoresizingMaskIntoConstraints = false
        
        NSLayoutConstraint.activate([
            expandedView.widthAnchor.constraint(equalToConstant: 240),
            expandedView.heightAnchor.constraint(equalToConstant: 140),
            
            // Header container
            headerContainer.topAnchor.constraint(equalTo: expandedView.topAnchor, constant: 20),
            headerContainer.leadingAnchor.constraint(equalTo: expandedView.leadingAnchor, constant: 24),
            headerContainer.trailingAnchor.constraint(equalTo: expandedView.trailingAnchor, constant: -24),
            headerContainer.heightAnchor.constraint(equalToConstant: 24),
            
            // Icon and title in header
            iconLabel.leadingAnchor.constraint(equalTo: headerContainer.leadingAnchor),
            iconLabel.centerYAnchor.constraint(equalTo: headerContainer.centerYAnchor),
            
            titleLabel.leadingAnchor.constraint(equalTo: iconLabel.trailingAnchor, constant: 12),
            titleLabel.trailingAnchor.constraint(equalTo: headerContainer.trailingAnchor),
            titleLabel.centerYAnchor.constraint(equalTo: headerContainer.centerYAnchor),
            
            // Subtitle
            subtitleLabel.topAnchor.constraint(equalTo: headerContainer.bottomAnchor, constant: 8),
            subtitleLabel.leadingAnchor.constraint(equalTo: expandedView.leadingAnchor, constant: 24),
            subtitleLabel.trailingAnchor.constraint(equalTo: expandedView.trailingAnchor, constant: -24),
            
            // Button container
            buttonContainer.topAnchor.constraint(equalTo: subtitleLabel.bottomAnchor, constant: 16),
            buttonContainer.leadingAnchor.constraint(equalTo: expandedView.leadingAnchor, constant: 24),
            buttonContainer.trailingAnchor.constraint(equalTo: expandedView.trailingAnchor, constant: -24),
            buttonContainer.bottomAnchor.constraint(equalTo: expandedView.bottomAnchor, constant: -20),
            buttonContainer.heightAnchor.constraint(equalToConstant: 40),
            
            // Buttons
            viewButton.leadingAnchor.constraint(equalTo: buttonContainer.leadingAnchor),
            viewButton.centerYAnchor.constraint(equalTo: buttonContainer.centerYAnchor),
            viewButton.widthAnchor.constraint(equalToConstant: 100),
            viewButton.heightAnchor.constraint(equalToConstant: 40),
            
            dismissButton.trailingAnchor.constraint(equalTo: buttonContainer.trailingAnchor),
            dismissButton.centerYAnchor.constraint(equalTo: buttonContainer.centerYAnchor),
            dismissButton.widthAnchor.constraint(equalToConstant: 80),
            dismissButton.heightAnchor.constraint(equalToConstant: 40)
        ])
    }
    
    private func setupGestures() {
        panGesture = UIPanGestureRecognizer(target: self, action: #selector(handlePan(_:)))
        tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTap(_:)))
        
        bubbleView.addGestureRecognizer(panGesture)
        bubbleView.addGestureRecognizer(tapGesture)
        bubbleView.isUserInteractionEnabled = true
    }
    
    private func positionBubble() {
        let safeArea = view.safeAreaLayoutGuide
        
        NSLayoutConstraint.activate([
            bubbleView.topAnchor.constraint(equalTo: safeArea.topAnchor, constant: 100),
            bubbleView.trailingAnchor.constraint(equalTo: safeArea.trailingAnchor, constant: -20)
        ])
    }
    
    @objc private func handlePan(_ gesture: UIPanGestureRecognizer) {
        let translation = gesture.translation(in: view)
        
        switch gesture.state {
        case .began:
            // Show delete area when dragging starts
            deleteArea.isHidden = false
            UIView.animate(withDuration: 0.2) {
                self.deleteArea.alpha = 1.0
            }
            
        case .changed:
            if let gestureView = gesture.view {
                gestureView.center = CGPoint(
                    x: gestureView.center.x + translation.x,
                    y: gestureView.center.y + translation.y
                )
                gesture.setTranslation(.zero, in: view)
                
                // Check if bubble is in delete zone
                let bubbleCenter = gestureView.center
                let deleteCenter = deleteArea.center
                let distance = sqrt(pow(bubbleCenter.x - deleteCenter.x, 2) + pow(bubbleCenter.y - deleteCenter.y, 2))
                
                let wasInDeleteZone = isInDeleteZone
                isInDeleteZone = distance < 100
                
                // Animate delete area when bubble enters/exits
                if isInDeleteZone != wasInDeleteZone {
                    UIView.animate(withDuration: 0.2) {
                        self.deleteArea.transform = self.isInDeleteZone ? CGAffineTransform(scaleX: 1.3, y: 1.3) : CGAffineTransform.identity
                    }
                }
            }
            
        case .ended:
            // Hide delete area
            UIView.animate(withDuration: 0.2) {
                self.deleteArea.alpha = 0.0
                self.deleteArea.transform = CGAffineTransform.identity
            } completion: { _ in
                self.deleteArea.isHidden = true
            }
            
            // Check if bubble should be deleted
            if isInDeleteZone {
                // Delete bubble with animation
                UIView.animate(withDuration: 0.3, animations: {
                    self.view.alpha = 0.0
                    self.view.transform = CGAffineTransform(scaleX: 0.1, y: 0.1)
                }) { _ in
                    // Notify parent to remove bubble
                    if let parent = self.parent {
                        parent.dismiss(animated: false)
                    }
                }
            } else {
                // Snap to edges
                snapToEdge()
            }
            
            isInDeleteZone = false
            
        default:
            break
        }
    }
    
    @objc private func handleTap(_ gesture: UITapGestureRecognizer) {
        // Add touch feedback animation
        UIView.animate(withDuration: 0.1, animations: {
            self.bubbleView.transform = CGAffineTransform(scaleX: 0.95, y: 0.95)
        }) { _ in
            UIView.animate(withDuration: 0.15, delay: 0, usingSpringWithDamping: 0.6, initialSpringVelocity: 0.8, options: [], animations: {
                self.bubbleView.transform = CGAffineTransform.identity
            }, completion: { _ in
                self.toggleExpanded()
            })
        }
    }
    
    private func toggleExpanded() {
        isExpanded.toggle()
        
        if isExpanded {
            // Expand animation with spring effect
            bubbleView.isHidden = true
            expandedView.isHidden = false
            expandedView.center = bubbleView.center
            expandedView.alpha = 0
            expandedView.transform = CGAffineTransform(scaleX: 0.8, y: 0.8)
            
            UIView.animate(withDuration: 0.4, delay: 0, usingSpringWithDamping: 0.7, initialSpringVelocity: 0.5, options: [], animations: {
                self.expandedView.alpha = 1.0
                self.expandedView.transform = CGAffineTransform.identity
            }, completion: nil)
        } else {
            // Collapse animation
            UIView.animate(withDuration: 0.25, animations: {
                self.expandedView.alpha = 0
                self.expandedView.transform = CGAffineTransform(scaleX: 0.8, y: 0.8)
            }) { _ in
                self.expandedView.isHidden = true
                self.bubbleView.isHidden = false
                self.bubbleView.alpha = 0
                self.bubbleView.transform = CGAffineTransform(scaleX: 0.8, y: 0.8)
                
                UIView.animate(withDuration: 0.3, delay: 0, usingSpringWithDamping: 0.6, initialSpringVelocity: 0.8, options: [], animations: {
                    self.bubbleView.alpha = 1.0
                    self.bubbleView.transform = CGAffineTransform.identity
                }, completion: nil)
            }
        }
    }
    
    private func snapToEdge() {
        let screenBounds = UIScreen.main.bounds
        let screenWidth = screenBounds.width
        let screenHeight = screenBounds.height
        let bubbleCenter = bubbleView.center
        let bubbleSize: CGFloat = 100
        
        // Calculate distances to each edge
        let distanceToLeft = bubbleCenter.x
        let distanceToRight = screenWidth - bubbleCenter.x
        let distanceToTop = bubbleCenter.y
        let distanceToBottom = screenHeight - bubbleCenter.y
        
        let minDistance = min(distanceToLeft, distanceToRight, distanceToTop, distanceToBottom)
        
        var targetCenter = bubbleCenter
        
        // Determine which edge is closest and snap to it
        if minDistance == distanceToLeft {
            // Snap to left edge
            targetCenter.x = 50
            targetCenter.y = max(100, min(screenHeight - 100, bubbleCenter.y))
        } else if minDistance == distanceToRight {
            // Snap to right edge
            targetCenter.x = screenWidth - 50
            targetCenter.y = max(100, min(screenHeight - 100, bubbleCenter.y))
        } else if minDistance == distanceToTop {
            // Snap to top edge
            targetCenter.y = 100
            targetCenter.x = max(100, min(screenWidth - 100, bubbleCenter.x))
        } else {
            // Snap to bottom edge
            targetCenter.y = screenHeight - 100
            targetCenter.x = max(100, min(screenWidth - 100, bubbleCenter.x))
        }
        
        // Smooth spring animation to edge
        UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.7, initialSpringVelocity: 0.5, options: [.curveEaseOut], animations: {
            self.bubbleView.center = targetCenter
        }, completion: nil)
    }
}