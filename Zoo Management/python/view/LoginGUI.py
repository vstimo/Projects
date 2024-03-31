import customtkinter
from tkinter import messagebox

from presenter.LoginPresenter import LoginPresenter
from view.AdminGUI import AdminGUI
from view.WorkerGUI import WorkerGUI
from view.GuestGUI import GuestGUI
from view.LoginInterface import LoginInterface

class LoginGUI(LoginInterface):
    def __init__(self):
        self.root = customtkinter.CTk()
        self.usernameEntry = None
        self.passwordEntry = None 
        self.loginPresenter = None 
        
        customtkinter.set_appearance_mode("Light")
        customtkinter.set_default_color_theme("green")
        
        screen_width = self.root.winfo_screenwidth()
        screen_height = self.root.winfo_screenheight()
        x_coordinate = 100+(screen_width - 400) // 2
        y_coordinate = (screen_height - 350) // 2
        self.root.geometry(f"400x350+{x_coordinate}+{y_coordinate}")
        
        frame = customtkinter.CTkFrame(master=self.root)
        frame.pack(pady=30, padx=60, fill="both", expand=False)

        label = customtkinter.CTkLabel(master=frame, text="ZOO PARK SYSTEM", font=("Segoe UI Black",24))
        label.pack(pady=12, padx=10)

        self.usernameEntry = customtkinter.CTkEntry(master=frame, placeholder_text="Username")
        self.usernameEntry.pack(pady=12, padx=10)
        self.passwordEntry = customtkinter.CTkEntry(master=frame, placeholder_text="Password", show="*")
        self.passwordEntry.pack(pady=12, padx=10)

        buttonL = customtkinter.CTkButton(master=frame, text="Login", command=self.__loginClick)
        buttonL.pack(pady=12,padx=10)
        buttonG = customtkinter.CTkButton(master=frame, text="Login as a Guest", command=self.__loginGuestClick)
        buttonG.pack(pady=12,padx=10)
       
        self.loginPresenter=LoginPresenter(self)
        
    #events management
    def __loginClick(self):
        self.loginPresenter.findUserBy()
        
    def __loginGuestClick(self):
        self.setRootDisappear()
        guestGUI=GuestGUI(customtkinter.CTk())
        guestGUI.setRootAsNewWindow()
        guestGUI.root.mainloop()
        
    #interface methods implementation
    def setErrorMessage(self, titlu, message):
        messagebox.showerror(titlu, message)
    
    def getUsernameText(self):
        return self.usernameEntry.get()
    
    def getPasswordText(self):
        return self.passwordEntry.get()

    def getRoot(self):
        return self.root.get()
    
    def setRootDisappear(self):
        self.root.withdraw()
        
    def turnOnWorkerGUI(self, username):
        worker=WorkerGUI(username)
        worker.root.mainloop()
        
    def turnOnAdminGUI(self, username):
        admin = AdminGUI(username)
        admin.root.mainloop()
        
    def setUsernameText(self, username):
        self.usernameEntry.delete(0, customtkinter.END)  # Clear any existing text
        self.usernameEntry.insert(0, username)  # Insert the new username
        
    def setPasswordText(self, password):
        self.passwordEntry.delete(0, customtkinter.END)  # Clear any existing text
        self.passwordEntry.insert(0, password)  # Insert the new username