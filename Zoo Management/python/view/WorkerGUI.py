import customtkinter
from presenter.WorkerPresenter import WorkerPresenter
from view.GuestGUI import GuestGUI
from view.ZooWorkerGUI import ZooWorkerGUI
from PIL import Image, ImageTk
import os

class WorkerGUI:
    def __init__(self,username):
        self.root = customtkinter.CTk()
        self.displayFrame = None
        self.username = username
        self.workerPresenter = WorkerPresenter(self)

        customtkinter.set_appearance_mode("Light")
        customtkinter.set_default_color_theme("green")
        
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
        workerButton = customtkinter.CTkButton(master=menuFrame, text="Worker administrative", command=self.__showWorkerInterface)
        workerButton.pack(pady=0, padx=10, fill="x")
        
        # Frame for displaying interface
        self.displayFrame = customtkinter.CTkFrame(master=self.root)
        self.displayFrame.pack(side=customtkinter.LEFT, fill="both", expand=True)
        
        label = customtkinter.CTkLabel(master=self.displayFrame, text="Select an option from the left menu", font=("Segoe UI Black",24))
        label.pack(pady=12, padx=10)

    def __showGuestInterface(self):
        self.workerPresenter.resetFrame(self.displayFrame.winfo_children())

        guestInterface = GuestGUI(self.displayFrame)
        guestInterface.root.mainloop()
        
    def __showWorkerInterface(self):
        self.workerPresenter.resetFrame(self.displayFrame.winfo_children())
        
        workerInterface = ZooWorkerGUI(self.displayFrame, self.username)
        workerInterface.root.mainloop()