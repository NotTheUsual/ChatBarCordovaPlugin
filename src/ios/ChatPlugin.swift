//
//  ChatPlugin.swift
//  ChatPlugin
//
//  Created by James Hunter on 20/08/2015.
//  Copyright (c) 2015 JADH. All rights reserved.
//

import UIKit

@objc(ChatPlugin) class ChatPlugin: CDVPlugin, RoundUpChatBarDelegate {

    var command = CDVInvokedUrlCommand()
    var chatBar: RoundUpChatBar?

    func showBar(command: CDVInvokedUrlCommand) {
        self.command = command

        print("showing")
        if let parentView = self.webView.superview {
            self.chatBar = RoundUpChatBar(parentFrame: parentView.frame)
            self.chatBar!.delegate = self
            parentView.addSubview(self.chatBar!)

            NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(self.keyboardWasShown(_:)), name: UIKeyboardWillShowNotification, object: nil)
            NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(self.keyboardWillBeHidden(_:)), name: UIKeyboardWillHideNotification, object: nil)
        }

    }
    
    // MARK: Keyboard notifications
    
    func keyboardWasShown(notification: NSNotification) {
        guard let info = notification.userInfo,
            let keyboardEndFrame = info[UIKeyboardFrameEndUserInfoKey] else { return }

        let keyboardSize = keyboardEndFrame.CGRectValue().size
        
        if let chatBar = self.chatBar {
            chatBar.transform = CGAffineTransformMakeTranslation(0, -keyboardSize.height)
        }
    }
    
    func keyboardWillBeHidden(notification: NSNotification) {
        if let chatBar = self.chatBar {
            chatBar.transform = CGAffineTransformIdentity
        }
    }
    
    func chatBar(charBar: RoundUpChatBar, didSendMessage message: String) {
        sendPluginResponse(.Message(message))
    }

    func hideBar(command: CDVInvokedUrlCommand) {
        self.command = command

        guard let chatBar = self.chatBar else { return }
        chatBar.deselectInput()
        chatBar.removeFromSuperview()
    }
    
    func hideKeyboard(command: CDVInvokedUrlCommand) {
        chatBar?.deselectInput()
    }
    
    func chatBarDidBeginEditing(chatBar: RoundUpChatBar) {
        sendPluginResponse(.Focus)
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
