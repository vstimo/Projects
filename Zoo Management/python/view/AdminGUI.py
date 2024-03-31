import customtkinter
from presenter.AdminPresenter import AdminPresenter
from view.ZooAdminGUI import ZooAdminGUI
from view.GuestGUI import GuestGUI

class AdminGUI:
    def __init__(self,username):
        self.root = customtkinter.CTk()
        self.displayFrame = None
        self.username = username
        self.adminPresenter = AdminPresenter(self)
        
        # Make the window appear in the middle of the screen
        screen_width = self.root.winfo_screenwidth()
        screen_height = self.root.winfo_screenheight()
        x_coordinate = 100 + (screen_width - 800) // 2
        y_coordinate = (screen_height - 600) // 2
        self.root.geometry(f"1000x600+{x_coordinate}+{y_coordinate}")
        
        # Menu frame
        menuFrame = customtkinter.CTkFrame(master=self.root)
        menuFrame.pack(side=customtkinter.LEFT, fill="y")
        
        # Menu options
        guestButton = customtkinter.CTkButton(master=menuFrame, text="See all animals as a guest", command=self.__showGuestInterface)
        guestButton.pack(pady=150, padx=10, fill="x")
        
        adminButton = customtkinter.CTkButton(master=menuFrame, text="Admin functionalities", command=self.__showAdminInterface)
        adminButton.pack(pady=0, padx=10, fill="x")
        
        # Frame for displaying interface
        self.displayFrame = customtkinter.CTkFrame(master=self.root)
        self.displayFrame.pack(side=customtkinter.LEFT, fill="both", expand=True)
        
        label = customtkinter.CTkLabel(master=self.displayFrame, text="Select an option from the left menu", font=("Segoe UI Black",24))
        label.pack(pady=12, padx=10)
        
    def __showGuestInterface(self):
        self.adminPresenter.resetFrame(self.displayFrame.winfo_children())

        guestInterface = GuestGUI(self.displayFrame)
        guestInterface.root.mainloop()
        
    def __showAdminInterface(self):
        self.adminPresenter.resetFrame(self.displayFrame.winfo_children())
        
        adminInterface = ZooAdminGUI(self.displayFrame, self.username)
        adminInterface.root.mainloop()