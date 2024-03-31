from tkinter import messagebox
import customtkinter

from presenter.WorkerPresenter import WorkerPresenter

class ZooWorkerGUI:
    def __init__(self, rootSource, username):
        self.root = rootSource
        self.operationCombo = None
        self.operationsFrame = None
        self.idEntry = None
        self.nameEntry = None
        self.speciesEntry = None
        self.dietEntry = None
        self.habitatEntry = None
        self.operationButton=None
        self.workerPresenter = WorkerPresenter(self)
        self.searchCriteriaCombo = None
        self.searchAnimalCombo = None
        self.criteriaEntry = None
        self.showFrame = None
        
        customtkinter.set_appearance_mode("Light")
        customtkinter.set_default_color_theme("green")
        
        frame = customtkinter.CTkFrame(master=self.root)
        frame.pack(pady=30, padx=60, fill="both", expand=False)
        
        title = customtkinter.CTkLabel(master=frame, text=f"Welcome user {username}!", font=("Eras Demi ITC", 30))
        title.pack(pady=12, padx=10)
        label1 = customtkinter.CTkLabel(master=frame, text="Choose an operation", font=("Rockwell", 15))
        label1.pack(pady=12, padx=10, side=customtkinter.LEFT)
        
        # Combo box for operations
        operations = ["Add Animal", "Find Animal", "Update Animal", "Delete Animal"]
        self.operationCombo = customtkinter.CTkComboBox(master=frame, values=operations, command=self.__selectOperation, state='readonly')
        self.operationCombo.set("Add Animal")
        self.operationCombo.pack(pady=10, padx=10, side=customtkinter.LEFT)
        
        self.operationsFrame = customtkinter.CTkFrame(master=self.root)
        self.operationsFrame.pack(pady=0, padx=0, fill="both", expand=True, side=customtkinter.LEFT)
    
    def __selectOperation(self, event=None):
        selected_operation = self.operationCombo.get()
        if selected_operation == "Add Animal": self.__createAddAnimalFrame()
        elif selected_operation == "Find Animal": self.__createFindAnimalFrame()
        elif selected_operation == "Update Animal": self.__createUpdateAnimalFrame()
        elif selected_operation == "Delete Animal": self.__createDeleteAnimalFrame()
            
    def __createAddAnimalFrame(self):
        self.workerPresenter.resetFrame(self.operationsFrame.winfo_children())
        
        titleAdd = customtkinter.CTkLabel(master=self.operationsFrame, text="Add a new animal to the ZOO", font=("Rockwell", 20))
        titleAdd.pack(pady=12, padx=10, side=customtkinter.TOP, anchor="w")
        
        frame1 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame1.pack(pady=5, padx=10, fill="both", expand=False)
        idLabel = customtkinter.CTkLabel(master=frame1, text="Enter the ID: ", font=("Rockwell", 15))
        idLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.idEntry = customtkinter.CTkEntry(master=frame1)
        self.idEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        frame2 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame2.pack(pady=5, padx=10, fill="both", expand=False)
        nameLabel = customtkinter.CTkLabel(master=frame2, text="Enter name of animal: ", font=("Rockwell", 15))
        nameLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.nameEntry = customtkinter.CTkEntry(master=frame2)
        self.nameEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        frame3 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame3.pack(pady=5, padx=10, fill="both", expand=False)
        speciesLabel = customtkinter.CTkLabel(master=frame3, text="Enter species of it: ", font=("Rockwell", 15))
        speciesLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.speciesEntry = customtkinter.CTkEntry(master=frame3)
        self.speciesEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        frame4 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame4.pack(pady=5, padx=10, fill="both", expand=False)
        dietLabel = customtkinter.CTkLabel(master=frame4, text="Enter diet of it: ", font=("Rockwell", 15))
        dietLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.dietEntry = customtkinter.CTkEntry(master=frame4)
        self.dietEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        frame5 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame5.pack(pady=5, padx=10, fill="both", expand=False)
        habitatLabel = customtkinter.CTkLabel(master=frame5, text="Enter habitat of it: ", font=("Rockwell", 15))
        habitatLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.habitatEntry = customtkinter.CTkEntry(master=frame5)
        self.habitatEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        self.operationButton = customtkinter.CTkButton(master=self.operationsFrame, text="Add animal to DataBase", font=("Rockwell", 15), command=self.__addButtonClick)
        self.operationButton.pack(pady=5, padx=10, side=customtkinter.LEFT)
        
    def __addButtonClick(self):
        self.workerPresenter.addAnimal()
    
    def __createFindAnimalFrame(self):
        self.workerPresenter.resetFrame(self.operationsFrame.winfo_children())
        
        titleAdd = customtkinter.CTkLabel(master=self.operationsFrame, text="Find animal(s) by a specified criteria", font=("Rockwell", 20))
        titleAdd.pack(pady=12, padx=10, side=customtkinter.TOP, anchor="w")
        
        frame1 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame1.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame1, text="Choose criteria: ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        search_criteria = ["ID", "Name", "Species", "Diet", "Habitat"]
        self.searchCriteriaCombo = customtkinter.CTkComboBox(master=frame1, values=search_criteria, state='readonly')
        self.searchCriteriaCombo.set("ID")
        self.searchCriteriaCombo.pack(pady=10, padx=10, side=customtkinter.LEFT, anchor="w")
        self.criteriaEntry = customtkinter.CTkEntry(master=frame1)
        self.criteriaEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        self.operationButton = customtkinter.CTkButton(master=self.operationsFrame, text="Search for animal", font=("Rockwell", 15), command=self.__searchAnimalClick)
        self.operationButton.pack(pady=5, padx=10, side=customtkinter.TOP, anchor="w")
        
        self.showFrame = customtkinter.CTkScrollableFrame(master=self.operationsFrame)
        self.showFrame.pack(pady=20, padx=20, fill="both", expand=True)
        
    def __searchAnimalClick(self):
        self.workerPresenter.resetFrame(self.showFrame.winfo_children())
        self.workerPresenter.findAnimalsBy()

    def __createUpdateAnimalFrame(self):
        self.workerPresenter.resetFrame(self.operationsFrame.winfo_children())
        
        titleAdd = customtkinter.CTkLabel(master=self.operationsFrame, text="Select an animal and update its information", font=("Rockwell", 20))
        titleAdd.pack(pady=12, padx=10, side=customtkinter.TOP, anchor="w")

        frame1 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame1.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame1, text="Select an animal ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.workerPresenter.findAllAnimals(frame1)
        
        frame2 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame2.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame2, text="Select which field to update ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        search_criteria = ["ID", "Name", "Species", "Diet", "Habitat"]
        self.searchCriteriaCombo = customtkinter.CTkComboBox(master=frame2, values=search_criteria, state='readonly')
        self.searchCriteriaCombo.set("ID")
        self.searchCriteriaCombo.pack(pady=10, padx=10, side=customtkinter.LEFT, anchor="w")
        
        frame3 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame3.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame3, text="Enter the value to be updated with: ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.criteriaEntry = customtkinter.CTkEntry(master=frame3)
        self.criteriaEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        self.operationButton = customtkinter.CTkButton(master=self.operationsFrame, text="Update animal information", font=("Rockwell", 15), command=self.__updateButtonClick)
        self.operationButton.pack(pady=5, padx=10, side=customtkinter.LEFT)
        
    def __updateButtonClick(self):
        self.workerPresenter.updateAnimal()
    
    def __createDeleteAnimalFrame(self):
        self.workerPresenter.resetFrame(self.operationsFrame.winfo_children())
        
        titleAdd = customtkinter.CTkLabel(master=self.operationsFrame, text="Select an animal to delete from database", font=("Rockwell", 20))
        titleAdd.pack(pady=12, padx=10, side=customtkinter.TOP, anchor="w")
        
        frame1 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame1.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame1, text="Select an animal ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.workerPresenter.findAllAnimals(frame1)
        
        self.operationButton = customtkinter.CTkButton(master=self.operationsFrame, text="Delete animal", font=("Rockwell", 15), command=self.__deleteButtonClick)
        self.operationButton.pack(pady=5, padx=10, side=customtkinter.LEFT)
    
    def __deleteButtonClick(self):
        self.workerPresenter.deleteAnimal()

    #interface methods implementation
    def getIdEntry(self):
        return self.idEntry.get()
    
    def getNameEntry(self):
        return self.nameEntry.get()
    
    def getSpeciesEntry(self):
        return self.speciesEntry.get()
    
    def getDietEntry(self):
        return self.dietEntry.get()
    
    def getHabitatEntry(self):
        return self.habitatEntry.get()
    
    def setErrorMessage(self, titlu, message):
        messagebox.showerror(titlu, message)
        
    def setInfoMessage(self, titlu, message):
        messagebox.showinfo(titlu, message)
        
    def getSearchCriteriaCombo(self):
        return self.searchCriteriaCombo.get()
    
    def setSearchAnimalCombo(self, values, frame1):
        self.searchAnimalCombo = customtkinter.CTkComboBox(master=frame1, values=values, state='readonly')
        self.searchAnimalCombo.set(values[0])
        self.searchAnimalCombo.pack(pady=10, padx=10, side=customtkinter.LEFT, anchor="w")
    
    def getCriteriaEntry(self):
        return self.criteriaEntry.get()
    
    def getSearchAnimalCombo(self):
        return self.searchAnimalCombo.get()
    
    def setShowFrame(self, label_text):
        labelF = customtkinter.CTkLabel(master=self.showFrame, text=label_text, font=("Rockwell", 14))
        labelF.pack(pady=0, padx=20, side=customtkinter.TOP, anchor="w")