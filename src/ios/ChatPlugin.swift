//
//  ChatPlugin.swift
//  ChatPlugin
//
//  Created by James Hunter on 20/08/2015.
//  Copyright (c) 2015 JADH. All rights reserved.
//

import UIKit

@objc(ChatPlugin) class ChatPlugin: CDVPlugin, UITextFieldDelegate, UITextViewDelegate {

    var command = CDVInvokedUrlCommand()
    var footerView: UIView?
//    var textView: UITextField?
    var newTextView: UITextView?
    var keyboardHeight: CGFloat = 0.0
    var sendButon: UIButton?
    
    struct Colours {
        static let inputBorder = UIColor(red: 0.753, green: 0.753, blue: 0.752, alpha: 1.0)
        static let wrapperBackground = UIColor(red: 0.949, green: 0.949, blue: 0.949, alpha: 1.0)
        static let inputBackground = UIColor.whiteColor()
        static let buttonBackground = UIColor(red: 0.988, green: 0.337, blue: 0.639, alpha: 1.0)
    }

    func showBar(command: CDVInvokedUrlCommand) {
        self.command = command

        print("showing")
        if let parentView = self.webView.superview {
            let parentHeight = parentView.frame.height
            let parentWidth = parentView.frame.width
            let wrapperHeight: CGFloat = 43.5 //50.0
            let wrapperView = UIView(frame: CGRect(x: 0.0, y: parentHeight - wrapperHeight, width: parentWidth, height: wrapperHeight))
            wrapperView.backgroundColor = Colours.wrapperBackground
            
            NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(self.keyboardWasShown(_:)), name: UIKeyboardWillShowNotification, object: nil)
            NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(self.keyboardWillBeHidden(_:)), name: UIKeyboardWillHideNotification, object: nil)
            
//            let textField = UITextField(frame: CGRect(x: 5.0, y: 5.0, width: parentWidth - 100.0, height: 40.0))
//            textField.backgroundColor = UIColor.whiteColor()
//            textField.placeholder = "Message here"
//            textField.layer.cornerRadius = 5.0
//            let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: 10, height: 40))
//            textField.leftView = paddingView
//            textField.leftViewMode = .Always
//            textField.delegate = self
//            
//            self.textView = textField
            
            let textViewHeight: CGFloat = wrapperHeight - 10.0
            let textView = UITextView(frame: CGRect(x: 5.0, y: 5.0, width: parentWidth - 100.0, height: textViewHeight))
            textView.backgroundColor = Colours.inputBackground
            textView.layer.borderColor = Colours.inputBorder.CGColor
            textView.layer.borderWidth = 1.0
            textView.layer.cornerRadius = 5.0
            textView.textContainerInset = UIEdgeInsets(top: 7, left: 5, bottom: 7, right: 5)
            textView.font = UIFont.systemFontOfSize(16)
            textView.text = "Message"
            textView.textColor = UIColor.lightGrayColor()
            textView.scrollEnabled = false
            textView.delegate = self
            self.newTextView = textView
            
            
            let sendButton = UIButton(frame: CGRect(x: textView.frame.width + 20.0, y: 6.0, width: 65.0, height: textViewHeight - 2.0))
            sendButton.backgroundColor = Colours.buttonBackground
            sendButton.layer.cornerRadius = 4.0
            sendButton.setTitle("Send", forState: .Normal)
            sendButton.setTitleColor(UIColor.whiteColor(), forState: .Normal)
            sendButton.titleLabel?.font = UIFont.systemFontOfSize(15)
            sendButton.addTarget(self, action: #selector(self.sendButtonTapped(_:)), forControlEvents: .TouchUpInside)
            self.sendButon = sendButton
            
//            wrapperView.addSubview(self.textView!)
            wrapperView.addSubview(self.newTextView!)
            wrapperView.addSubview(sendButton)
            
            self.footerView = wrapperView
            
            parentView.addSubview(self.footerView!)
        }

    }
    
    func keyboardWasShown(notification: NSNotification) {
        guard let info = notification.userInfo,
            let keyboardEndFrame = info[UIKeyboardFrameEndUserInfoKey] else { return }

        let keyboardSize = keyboardEndFrame.CGRectValue().size
        
        if let messageView = self.footerView {
            messageView.transform = CGAffineTransformMakeTranslation(0, -keyboardSize.height)
            self.keyboardHeight = keyboardSize.height
        }
    }
    
    func keyboardWillBeHidden(notification: NSNotification) {
        if let messageView = self.footerView {
            messageView.transform = CGAffineTransformIdentity
        }
    }
    
    func sendButtonTapped(sender: UIButton!) {
        print("Sending: \(self.newTextView!.text)")
        sendPluginResponse(.Message(self.newTextView!.text!))
        
        self.newTextView!.text = ""
    }

    func hideBar(command: CDVInvokedUrlCommand) {
        self.command = command

        self.newTextView?.resignFirstResponder()
        self.footerView!.removeFromSuperview()
    }
    
    func hideKeyboard(command: CDVInvokedUrlCommand) {
        self.newTextView?.resignFirstResponder()
        self.newTextView?.resignFirstResponder()
    }
    
    func textFieldShouldBeginEditing(textField: UITextField) -> Bool {
        print("should begin editing")
        return true
    }
    
    func textFieldDidBeginEditing(textField: UITextField) {
        sendPluginResponse(.Focus)
    }
    
    func textViewDidBeginEditing(textView: UITextView) {
        if textView.textColor == UIColor.lightGrayColor() {
            textView.text = ""
            textView.textColor = UIColor.blackColor()
        }
        sendPluginResponse(.Focus)
    }
    
    func textViewDidChange(textView: UITextView) {
        let maxLines: CGFloat = 6.0;
        let maxHeight = textView.font!.lineHeight * maxLines
//        textView.sizeThatFits(CGSize(width: textView.frame.width, height: maxHeight))
//        textView.sizeToFit()
//        textView.layoutIfNeeded()
        let fixedWidth = textView.frame.width
        let newSize = textView.sizeThatFits(CGSize(width: fixedWidth, height: maxHeight))
        var newFrame = textView.frame
        newFrame.size = CGSize(width: (max(newSize.width, fixedWidth)), height: newSize.height)
        textView.frame = newFrame
        
        let newWrapperHeight = newFrame.height + 10.0
        self.footerView!.frame.origin.y -= (newWrapperHeight - self.footerView!.frame.height)
        self.footerView!.frame.size.height = newWrapperHeight
        
        let newButtonOrigin = newWrapperHeight - 6.0 - self.sendButon!.frame.height
        self.sendButon!.frame.origin.y = newButtonOrigin
    }
    
    func textViewDidEndEditing(textView: UITextView) {
        if textView.text == "" {
            textView.text = "Message"
            textView.textColor = UIColor.lightGrayColor()
        }
    }
    
    private func sendPluginResponse(response: Response) {
        let responseDictionary = responseAsDictionary(response)
        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAsDictionary: responseDictionary)
        result.setKeepCallbackAsBool(true)
        self.commandDelegate!.sendPluginResult(result, callbackId: command.callbackId)
    }
    
    private func responseAsDictionary(response: Response) -> [String: AnyObject] {
        var dictionary = [String: AnyObject]()
        dictionary["action"] = response.name()
        
        if case .Message(let text) = response {
            dictionary["message"] = text
        }
        
        return dictionary
    }
    
    private enum Response {
        case Normal
        case Focus
        case Message(String)
        
        func name() -> String {
            return String(self)
                .lowercaseString
                .componentsSeparatedByString("(")[0]
        }
    }
}
