#!/usr/bin/env python3
"""
WhatsOrder TWA Documentation Server
Displays project documentation, UML diagrams, and build instructions
"""

from http.server import HTTPServer, SimpleHTTPRequestHandler
import markdown
import os
from pathlib import Path

class DocHandler(SimpleHTTPRequestHandler):
    def do_GET(self):
        if self.path == '/' or self.path == '/index.html':
            self.send_response(200)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            
            # Read README
            readme_content = Path('README.md').read_text()
            readme_html = markdown.markdown(readme_content, extensions=['fenced_code', 'tables'])
            
            # Create HTML page
            html = f"""
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WhatsOrder TWA - Documentation</title>
    <style>
        * {{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }}
        
        body {{
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
            line-height: 1.6;
            color: #333;
            background: #f5f5f5;
        }}
        
        .header {{
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }}
        
        .header h1 {{
            font-size: 2.5rem;
            margin-bottom: 0.5rem;
        }}
        
        .header p {{
            font-size: 1.1rem;
            opacity: 0.9;
        }}
        
        .nav {{
            background: white;
            padding: 1rem;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            position: sticky;
            top: 0;
            z-index: 100;
        }}
        
        .nav-links {{
            display: flex;
            justify-content: center;
            gap: 2rem;
            list-style: none;
            flex-wrap: wrap;
        }}
        
        .nav-links a {{
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            padding: 0.5rem 1rem;
            border-radius: 5px;
            transition: all 0.3s;
        }}
        
        .nav-links a:hover {{
            background: #667eea;
            color: white;
        }}
        
        .container {{
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }}
        
        .card {{
            background: white;
            padding: 2rem;
            margin-bottom: 2rem;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }}
        
        .card h2 {{
            color: #667eea;
            margin-bottom: 1rem;
            padding-bottom: 0.5rem;
            border-bottom: 2px solid #e0e0e0;
        }}
        
        .features {{
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
            margin-top: 2rem;
        }}
        
        .feature {{
            padding: 1.5rem;
            background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
            border-radius: 8px;
            border-left: 4px solid #667eea;
        }}
        
        .feature h3 {{
            color: #667eea;
            margin-bottom: 0.5rem;
        }}
        
        .quick-links {{
            display: flex;
            gap: 1rem;
            margin-top: 1.5rem;
            flex-wrap: wrap;
        }}
        
        .btn {{
            display: inline-block;
            padding: 0.75rem 1.5rem;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: all 0.3s;
            font-weight: 500;
        }}
        
        .btn:hover {{
            background: #5568d3;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.3);
        }}
        
        .btn-secondary {{
            background: #764ba2;
        }}
        
        .btn-secondary:hover {{
            background: #653a8e;
        }}
        
        code {{
            background: #f4f4f4;
            padding: 0.2rem 0.4rem;
            border-radius: 3px;
            font-family: 'Courier New', monospace;
        }}
        
        pre {{
            background: #2d2d2d;
            color: #f8f8f2;
            padding: 1rem;
            border-radius: 5px;
            overflow-x: auto;
            margin: 1rem 0;
        }}
        
        pre code {{
            background: none;
            color: inherit;
        }}
        
        table {{
            width: 100%;
            border-collapse: collapse;
            margin: 1rem 0;
        }}
        
        th, td {{
            padding: 0.75rem;
            text-align: left;
            border-bottom: 1px solid #e0e0e0;
        }}
        
        th {{
            background: #f8f9fa;
            font-weight: 600;
            color: #667eea;
        }}
        
        .footer {{
            text-align: center;
            padding: 2rem;
            background: #2d2d2d;
            color: white;
            margin-top: 3rem;
        }}
        
        @media (max-width: 768px) {{
            .header h1 {{
                font-size: 1.8rem;
            }}
            
            .nav-links {{
                flex-direction: column;
                gap: 0.5rem;
            }}
        }}
    </style>
</head>
<body>
    <div class="header">
        <h1>📱 WhatsOrder TWA</h1>
        <p>Android Trusted Web Activity with AdMob Integration</p>
    </div>
    
    <nav class="nav">
        <ul class="nav-links">
            <li><a href="#overview">Overview</a></li>
            <li><a href="#uml">UML Diagrams</a></li>
            <li><a href="#build">Build Instructions</a></li>
            <li><a href="#architecture">Architecture</a></li>
            <li><a href="UML_DIAGRAMS.md" target="_blank">Full UML Docs</a></li>
        </ul>
    </nav>
    
    <div class="container">
        <div class="card">
            <h2>🚀 Quick Start</h2>
            <p>This is an Android TWA (Trusted Web Activity) app that wraps the WhatsOrder web application with integrated AdMob advertising.</p>
            
            <div class="quick-links">
                <a href="UML_DIAGRAMS.md" target="_blank" class="btn">View UML Diagrams</a>
                <a href="README.md" target="_blank" class="btn btn-secondary">Read Full Documentation</a>
            </div>
        </div>
        
        <div class="card">
            <h2>✨ Key Features</h2>
            <div class="features">
                <div class="feature">
                    <h3>🎨 Splash Screen</h3>
                    <p>Interstitial ad on app launch with timeout fallback</p>
                </div>
                <div class="feature">
                    <h3>🌐 WebView Integration</h3>
                    <p>Seamless web app experience with back navigation</p>
                </div>
                <div class="feature">
                    <h3>💰 AdMob Monetization</h3>
                    <p>Banner and interstitial ads for revenue generation</p>
                </div>
                <div class="feature">
                    <h3>📱 Native Experience</h3>
                    <p>Fully native Android app with lifecycle management</p>
                </div>
            </div>
        </div>
        
        <div class="card" id="build">
            <h2>🔨 Build Instructions</h2>
            <h3>Option 1: GitHub Actions (Recommended)</h3>
            <ol>
                <li>Push this code to a GitHub repository</li>
                <li>GitHub Actions automatically builds the APK</li>
                <li>Download from Actions tab artifacts</li>
            </ol>
            
            <h3>Option 2: Local Build</h3>
            <pre><code>cd VercelAdTWA/VercelAdTWA
chmod +x gradlew
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk</code></pre>
            
            <h3>Option 3: Replit</h3>
            <p>Use Replit for documentation and code editing. Build APKs using GitHub Actions or local Android Studio.</p>
        </div>
        
        <div class="card" id="architecture">
            <h2>🏗️ Architecture Overview</h2>
            <h3>Project Structure</h3>
            <pre><code>VercelAdTWA/VercelAdTWA/
├── app/
│   ├── src/main/
│   │   ├── java/com/whatsorder/twa/
│   │   │   ├── LauncherActivity.kt    # Splash + Interstitial
│   │   │   └── MainActivity.kt        # WebView + Banner
│   │   ├── res/                       # Resources
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle</code></pre>
            
            <h3>Technical Specs</h3>
            <ul>
                <li><strong>Min SDK:</strong> 21 (Android 5.0)</li>
                <li><strong>Target SDK:</strong> 34 (Android 14)</li>
                <li><strong>Language:</strong> Kotlin</li>
                <li><strong>Build System:</strong> Gradle 8.1.1</li>
            </ul>
        </div>
        
        <div class="card" id="uml">
            <h2>📊 UML Documentation</h2>
            <p>Comprehensive UML diagrams are available including:</p>
            <ul>
                <li>Class Diagrams - Component relationships and structure</li>
                <li>Sequence Diagrams - Activity flow and interactions</li>
                <li>State Diagrams - Lifecycle and state management</li>
                <li>Architecture Diagrams - System overview</li>
            </ul>
            <a href="UML_DIAGRAMS.md" target="_blank" class="btn">View Full UML Documentation</a>
        </div>
        
        <div class="card">
            <h2>📦 Dependencies</h2>
            <table>
                <thead>
                    <tr>
                        <th>Library</th>
                        <th>Version</th>
                        <th>Purpose</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>AndroidX Core KTX</td>
                        <td>1.12.0</td>
                        <td>Kotlin extensions</td>
                    </tr>
                    <tr>
                        <td>AndroidX AppCompat</td>
                        <td>1.6.1</td>
                        <td>Backward compatibility</td>
                    </tr>
                    <tr>
                        <td>Google Play Services Ads</td>
                        <td>22.6.0</td>
                        <td>AdMob monetization</td>
                    </tr>
                    <tr>
                        <td>AndroidX WebKit</td>
                        <td>1.8.0</td>
                        <td>WebView functionality</td>
                    </tr>
                    <tr>
                        <td>ConstraintLayout</td>
                        <td>2.1.4</td>
                        <td>Layout management</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    
    <div class="footer">
        <p>WhatsOrder TWA Documentation Server</p>
        <p>Built with ❤️ for Android Development</p>
    </div>
</body>
</html>
"""
            self.wfile.write(html.encode())
        else:
            # Serve other files normally
            super().do_GET()

def run_server(port=5000):
    server_address = ('0.0.0.0', port)
    httpd = HTTPServer(server_address, DocHandler)
    print(f'Documentation server running on http://0.0.0.0:{port}')
    print(f'Visit http://localhost:{port} to view project documentation')
    httpd.serve_forever()

if __name__ == '__main__':
    run_server()
