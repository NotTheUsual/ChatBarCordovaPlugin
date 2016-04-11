//
//  RoundUpChatBar.swift
//  RoundUp
//
//  Created by James Hunter on 08/04/2016.
//
//

import Foundation

private struct Colours {
    static let inputBorder = UIColor(red: 0.753, green: 0.753, blue: 0.752, alpha: 1.0)
    static let wrapperBackground = UIColor(red: 0.949, green: 0.949, blue: 0.949, alpha: 1.0)
    static let inputBackground = UIColor.whiteColor()
    static let buttonBackground = UIColor(red: 0.988, green: 0.337, blue: 0.639, alpha: 1.0)
}

class RoundUpChatBar: UIView, UITextViewDelegate {
    
    var delegate: RoundUpChatBarDelegate?
    
    let textView: RoundUpChatBarInput
    let sendButton: UIButton
    
    // MARK: Initialization
    
    init(parentFrame: CGRect) {
        let wrapperHeight: CGFloat = 43.5
        let frame = CGRect(x: 0.0, y: parentFrame.height - wrapperHeight, width: parentFrame.width, height: wrapperHeight)
        
        let textViewWidth: CGFloat = parentFrame.width - 100.0
        let textViewHeight: CGFloat = wrapperHeight - 10.0
        self.textView = RoundUpChatBarInput(frame: CGRect(x: 5.0, y: 5.0, width: textViewWidth, height: textViewHeight))
        
        self.sendButton = RoundUpChatBar.sendButton(frame: CGRect(x: textViewWidth + 20.0, y: 6.0, width: 65.0, height: textViewHeight - 2.0))
        
        super.init(frame: frame)
        self.backgroundColor = Colours.wrapperBackground
        
        self.textView.delegate = self
        self.sendButton.addTarget(self, action: #selector(self.sendButtonTapped(_:)), forControlEvents: .TouchUpInside)
        
        self.addSubview(self.textView)
        self.addSubview(self.sendButton)
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
        
        let newWrapperHeight = newFrame.height + 10.0
        adjustWrapperHeightFor(newWrapperHeight)
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
    
    private func adjustButtonPositionFor(newWrapperHeight: CGFloat) {
        let newButtonOrigin = newWrapperHeight - 6.0 - self.sendButton.frame.height
        self.sendButton.frame.origin.y = newButtonOrigin
    }
    
    // MARK: Actions
    
    func sendButtonTapped(sender: UIButton!) {
        print("Sending: \(self.textView.text)")
        delegate?.chatBar(self, didSendMessage: self.textView.text)
        self.textView.text = ""
    }
}

protocol RoundUpChatBarDelegate {
    func chatBarDidBeginEditing(chatBar: RoundUpChatBar)
    func chatBar(charBar: RoundUpChatBar, didSendMessage message: String)
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
