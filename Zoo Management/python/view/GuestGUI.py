from tkinter import messagebox
import customtkinter

from presenter.GuestPresenter import GuestPresenter
from view.GuestInterface import GuestInterface

class GuestGUI(GuestInterface):
    def __init__(self, rootSource):
        self.root = rootSource
        self.frame = None
        self.showFrame = None
        self.filter_category = None
        self.filterEntry = None
        self.guestPresenter = GuestPresenter(self)
        
        customtkinter.set_appearance_mode("Light")
        customtkinter.set_default_color_theme("green")
        
        #Main frame
        self.frame = customtkinter.CTkFrame(master=self.root)
        self.frame.pack(pady=30, padx=60, fill="both", expand=False)

        title = customtkinter.CTkLabel(master=self.frame, text="WELCOME TO ZOO", font=("Eras Demi ITC", 36))
        title.pack(pady=12, padx=10)
        description = customtkinter.CTkLabel(master=self.frame, text="Choose a desired operation & find information about your favourite animal!", font=("Rockwell", 14))
        description.pack(pady=12, padx=10)
        
        # Create filter options with a new frame
        optionFrame = customtkinter.CTkFrame(master=self.frame)
        optionFrame.pack(pady=0, padx=20, fill="x", expand=False)
        
        labelF = customtkinter.CTkLabel(master=optionFrame, text="Filter animals based on:", font=("Rockwell", 14))
        labelF.pack(pady=0, padx=20, side=customtkinter.LEFT)
        
        # Filter categories dropdown menu
        filter_options = ["Species", "Diet", "Habitat"]
        self.filter_category = customtkinter.CTkComboBox(master=optionFrame, values=filter_options, state='readonly')
        self.filter_category.set("Species")
        self.filter_category.pack(side=customtkinter.LEFT, padx=(5, 5))
        
        # Text field for filter input
        self.filterEntry = customtkinter.CTkEntry(master=optionFrame)
        self.filterEntry.pack(side=customtkinter.LEFT)
        
        # Filter button
        filterButton = customtkinter.CTkButton(master=optionFrame, text="Filter", command=self.__filterAnimalsClick)
        filterButton.pack(side=customtkinter.LEFT, padx=(20, 5))
        allAnimalsButton = customtkinter.CTkButton(master=self.frame, text="See all animals", command=self.__showAllAnimalsClick)
        allAnimalsButton.pack(pady=12, padx=0, side=customtkinter.TOP)
        
        #A new frame for the outputs
        self.showFrame = customtkinter.CTkScrollableFrame(master=self.root)
        self.showFrame.pack(pady=20, padx=20, fill="both", expand=True)
    
    #events management
    def __showAllAnimalsClick(self):
        self.guestPresenter.resetFrame(self.showFrame.winfo_children())
        self.guestPresenter.findAllAnimalsSorted()
        
    def __filterAnimalsClick(self):
        self.guestPresenter.resetFrame(self.showFrame.winfo_children())
        self.guestPresenter.findAnimalsBy()
    
    #interface methods implementation
    def getSelectedCategory(self):
        return self.filter_category.get()
    
    def getFilterEntry(self):
        return self.filterEntry.get()
    
    def setErrorMessage(self, titlu, message):
        messagebox.showerror(titlu, message)
        
    def setShowFrame(self, label_text):
        labelF = customtkinter.CTkLabel(master=self.showFrame, text=label_text, font=("Rockwell", 14))
        labelF.pack(pady=0, padx=20, side=customtkinter.TOP, anchor="w")
        
    def setRootAsNewWindow(self):
        screen_width = self.root.winfo_screenwidth()
        screen_height = self.root.winfo_screenheight()
        x_coordinate = 100 + (screen_width - 800) // 2
        y_coordinate = (screen_height - 600) // 2
        self.root.geometry(f"800x600+{x_coordinate}+{y_coordinate}")