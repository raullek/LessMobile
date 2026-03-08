//
//  Initialization.swift
//  iosApp
//
//  Created by Raul  on 31.01.26.
//
import FirebaseCore
import SwiftUI

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    FirebaseApp.configure()

    print("Firebase was configured")

    return true
  }
}
