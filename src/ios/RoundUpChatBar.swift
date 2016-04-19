//
//  RoundUpChatBar.swift
//  RoundUp
//
//  Created by James Hunter on 08/04/2016.
//
//

import Foundation

private extension Selector {
    static let sendButtonTapped = #selector(RoundUpChatBar.sendButtonTapped(_:))
    static let messageBarTapped = #selector(RoundUpChatBar.messageBarTapped(_:))
}

private extension UIColor {
    convenience init(grey: CGFloat) {
        let value = grey / 255.0
        self.init(white: value, alpha: 1.0)
    }
    
    convenience init(red: Int, green: Int, blue: Int) {
        let redValue: CGFloat = CGFloat(red) / 255.0
        let greenValue: CGFloat = CGFloat(green) / 255.0
        let blueValue: CGFloat = CGFloat(blue) / 255.0
        self.init(red: redValue, green: greenValue, blue: blueValue, alpha: 1.0)
    }
}

private struct Colours {
    static let inputBorder = UIColor(grey: 192)
    static let wrapperBackground = UIColor(grey: 242)
    static let inputBackground = UIColor.whiteColor()
    static let buttonBackground = UIColor(red: 252, green: 86, blue: 163)
}

class RoundUpChatBar: UIView, UITextViewDelegate {
    
    var delegate: RoundUpChatBarDelegate?
    
    let backgroundView: UIView
    let textView: RoundUpChatBarInput
    let sendButton: UIButton
    let messageBar: UIButton
    
    // MARK: Initialization
    
    init(parentFrame: CGRect) {
        let barHeight: CGFloat = 43.5
        let wrapperHeight: CGFloat = barHeight + 30.0
        
        let wrapperFrame = CGRect(x: 0.0, y: parentFrame.height - wrapperHeight, width: parentFrame.width, height: wrapperHeight)

        self.backgroundView = UIView(frame: CGRect(x: 0, y: 30.0, width: wrapperFrame.width, height: barHeight))
        self.backgroundView.backgroundColor = Colours.wrapperBackground
        
        let textViewWidth: CGFloat = parentFrame.width - 95.0
        let textViewHeight: CGFloat = barHeight - 10.0
        self.textView = RoundUpChatBarInput(frame: CGRect(x: 10.0, y: 30.0 + 5.0, width: textViewWidth, height: textViewHeight))
        
        self.sendButton = RoundUpChatBar.sendButton(frame: CGRect(x: textViewWidth + 20.0, y: 30.0 + 6.0, width: 65.0, height: textViewHeight - 2.0))
        
        self.messageBar = RoundUpChatBar.newMessageBanner(frame: CGRect(x: (wrapperFrame.width / 2) - 80, y: 30, width: 160, height: 40))
        
        super.init(frame: wrapperFrame)
        
        self.textView.delegate = self
        self.sendButton.addTarget(self, action: .sendButtonTapped, forControlEvents: .TouchUpInside)
        self.messageBar.addTarget(self, action: .messageBarTapped, forControlEvents: .TouchUpInside)
        
        self.addSubview(self.backgroundView)
        self.addSubview(self.textView)
        self.addSubview(self.sendButton)
        self.addSubview(self.messageBar)
        self.sendSubviewToBack(self.messageBar)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private static func sendButton(frame frame: CGRect) -> UIButton {
        let button = UIButton(frame: frame)
        button.backgroundColor = Colours.buttonBackground
        button.layer.cornerRadius = 4.0
        button.setTitle("Send", forState: .Normal)
        button.setTitleColor(UIColor.whiteColor(), forState: .Normal)
        button.titleLabel?.font = UIFont.systemFontOfSize(15)
        return button
    }
    
    private static func newMessageBanner(frame frame: CGRect) -> UIButton {
        let messageBar = UIButton(frame: frame)
        messageBar.setTitle("1 New Message", forState: .Normal)
        messageBar.setTitleColor(.whiteColor(), forState: .Normal)
        messageBar.titleLabel?.font = UIFont.systemFontOfSize(13)
        messageBar.backgroundColor = Colours.buttonBackground
        messageBar.contentVerticalAlignment = .Top
        messageBar.titleEdgeInsets = UIEdgeInsets(top: 7.0, left: 0.0, bottom: 0.0, right: 0.0)
        
        let maskPath = UIBezierPath(roundedRect: messageBar.bounds, byRoundingCorners: [.TopLeft, .TopRight], cornerRadii: CGSize(width: 5, height: 5))
        let maskLayer = CAShapeLayer()
        maskLayer.frame = messageBar.bounds
        maskLayer.path = maskPath.CGPath
        messageBar.layer.mask = maskLayer
        
        return messageBar
    }
    
    override func pointInside(point: CGPoint, withEvent event: UIEvent?) -> Bool {
        for subview in subviews {
            if !subview.hidden && subview.alpha > 0 && subview.userInteractionEnabled && subview.pointInside(convertPoint(point, toView: subview), withEvent: event) {
                return true
            }
        }
        return false
    }
    
    func showMessageBar(count: Int) {
        let message = count == 1 ? "1 New Message" : "\(count) New Messages"
        messageBar.setTitle(message, forState: .Normal)
        
        UIView.animateWithDuration(0.25, delay: 0.0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: [], animations: { [unowned self] in
            self.messageBar.transform = CGAffineTransformMakeTranslation(0, -30)
            }, completion: nil)
    }
    
    // MARK: Text view functions
    
    func deselectInput() -> Bool {
        return self.textView.resignFirstResponder()
    }
    
    // MARK: Text View Delegate methods
    
    func textViewDidBeginEditing(textView: UITextView) {
        if textView.textColor == UIColor.lightGrayColor() {
            textView.text = ""
            textView.textColor = UIColor.blackColor()
        }
        delegate?.chatBarDidBeginEditing(self)
    }
    
    func textViewDidEndEditing(textView: UITextView) {
        if let input = textView as? RoundUpChatBarInput where textView.text == "" {
            input.reset()
        }
    }
    
    func textViewDidChange(textView: UITextView) {
        let newSize = newTextViewSize(textView)
        var newFrame = textView.frame
        newFrame.size = CGSize(width: (max(newSize.width, textView.frame.width)), height: newSize.height)
        textView.frame = newFrame
        
        let newWrapperHeight = newFrame.height + 10.0 + 30.0
        adjustWrapperHeightFor(newWrapperHeight)
        adjustBackgroundHeightFor(newWrapperHeight)
        adjustButtonPositionFor(newWrapperHeight)
    }
    
    private func newTextViewSize(textView: UITextView) -> CGSize {
        let maxLines: CGFloat = 6.0;
        let maxHeight = textView.font!.lineHeight * maxLines
        let fixedWidth = textView.frame.width
        return textView.sizeThatFits(CGSize(width: fixedWidth, height: maxHeight))
    }
    
    private func adjustWrapperHeightFor(newWrapperHeight: CGFloat) {
        self.frame.origin.y -= (newWrapperHeight - self.frame.height)
        self.frame.size.height = newWrapperHeight
    }
    
    private func adjustBackgroundHeightFor(newWrapperHeight: CGFloat) {
        let newBGHeight = newWrapperHeight - 30.0
        self.backgroundView.frame.size.height = newBGHeight
    }
    
    private func adjustButtonPositionFor(newWrapperHeight: CGFloat) {
        let newButtonOrigin = newWrapperHeight - 6.0 - self.sendButton.frame.height
        self.sendButton.frame.origin.y = newButtonOrigin
    }
    
    // MARK: Actions
    
    func sendButtonTapped(sender: UIButton!) {
        delegate?.chatBar(self, didSendMessage: self.textView.text)
        self.textView.text = ""
    }
    
    func messageBarTapped(sender: UIButton!) {
        UIView.animateWithDuration(0.1) {
            self.messageBar.transform = CGAffineTransformIdentity
        }
        delegate?.chatBarDidAcknowledgeNewMessage(self)
    }
}

protocol RoundUpChatBarDelegate {
    func chatBarDidBeginEditing(chatBar: RoundUpChatBar)
    func chatBar(charBar: RoundUpChatBar, didSendMessage message: String)
    func chatBarDidAcknowledgeNewMessage(chatBar: RoundUpChatBar)
}

class RoundUpChatBarInput: UITextView {
    init(frame: CGRect) {
        super.init(frame: frame, textContainer: nil)
        self.backgroundColor = Colours.inputBackground
        self.layer.borderColor = Colours.inputBorder.CGColor
        self.layer.borderWidth = 1.0
        self.layer.cornerRadius = 5.0
        self.textContainerInset = UIEdgeInsets(top: 7, left: 5, bottom: 7, right: 5)
        self.font = UIFont.systemFontOfSize(16)
        self.scrollEnabled = false
        reset()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func reset() {
        self.text = "Message"
        self.textColor = UIColor.lightGrayColor()
    }
}
