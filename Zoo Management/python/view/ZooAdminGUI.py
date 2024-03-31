from tkinter import messagebox
import customtkinter
from presenter.AdminPresenter import AdminPresenter
from view.AdminInterface import AdminInterface


class ZooAdminGUI(AdminInterface):
    def __init__(self, rootSource, username):
        self.root = rootSource
        self.operationCombo = None
        self.operationsFrame = None
        self.idEntry = None
        self.usernameEntry = None
        self.passwordEntry = None
        self.adminCheck = None
        self.operationButton=None
        self.adminPresenter =AdminPresenter(self)
        self.searchCriteriaCombo = None
        self.searchUserCombo = None
        self.criteriaEntry = None
        self.showFrame = None
        self.radio_no = None
        self.radio_yes = None
        self.adminChoice = None
        
        customtkinter.set_appearance_mode("Light")
        customtkinter.set_default_color_theme("green")
        
        frame = customtkinter.CTkFrame(master=self.root)
        frame.pack(pady=30, padx=60, fill="both", expand=False)
            
        title = customtkinter.CTkLabel(master=frame, text=f"Welcome admin {username}!", font=("Eras Demi ITC", 30))
        title.pack(pady=12, padx=10)
        label1 = customtkinter.CTkLabel(master=frame, text="Choose an operation", font=("Rockwell", 15))
        label1.pack(pady=12, padx=10, side=customtkinter.LEFT)
        
        # Combo box for operations
        operations = ["Add User", "Find User", "Update User", "Delete User", "See all Users"]
        self.operationCombo = customtkinter.CTkComboBox(master=frame, values=operations, command=self.__selectOperation, state='readonly')
        self.operationCombo.set("Add User")
        self.operationCombo.pack(pady=10, padx=10, side=customtkinter.LEFT)
        
        self.operationsFrame = customtkinter.CTkFrame(master=self.root)
        self.operationsFrame.pack(pady=0, padx=0, fill="both", expand=True, side=customtkinter.LEFT)
        
    def __selectOperation(self, event=None):
        selected_operation = self.operationCombo.get()
        if selected_operation == "Add User": self.__createAddUserFrame()
        elif selected_operation == "Find User": self.__createFindUserFrame()
        elif selected_operation == "Update User": self.__createUpdateUserFrame()
        elif selected_operation == "Delete User": self.__createDeleteUserFrame()
        elif selected_operation == "See all Users": self.__createSeeAllUsersFrame()
        
    def __createAddUserFrame(self):
        self.adminPresenter.resetFrame(self.operationsFrame.winfo_children())
        
        titleAdd = customtkinter.CTkLabel(master=self.operationsFrame, text="Add a new user to the ZOO database", font=("Rockwell", 20))
        titleAdd.pack(pady=12, padx=10, side=customtkinter.TOP, anchor="w")
        
        frame1 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame1.pack(pady=5, padx=10, fill="both", expand=False)
        idLabel = customtkinter.CTkLabel(master=frame1, text="Enter the ID: ", font=("Rockwell", 15))
        idLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.idEntry = customtkinter.CTkEntry(master=frame1)
        self.idEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        frame2 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame2.pack(pady=5, padx=10, fill="both", expand=False)
        usernameLabel = customtkinter.CTkLabel(master=frame2, text="Enter username of user: ", font=("Rockwell", 15))
        usernameLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.usernameEntry = customtkinter.CTkEntry(master=frame2)
        self.usernameEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        frame3 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame3.pack(pady=5, padx=10, fill="both", expand=False)
        passwordLabel = customtkinter.CTkLabel(master=frame3, text="Enter password of the user: ", font=("Rockwell", 15))
        passwordLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.passwordEntry = customtkinter.CTkEntry(master=frame3)
        self.passwordEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        frame4 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame4.pack(pady=5, padx=10, fill="both", expand=False)
        dietLabel = customtkinter.CTkLabel(master=frame4, text="Is the user an admin? ", font=("Rockwell", 15))
        dietLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        admin_choice = customtkinter.IntVar()
        self.adminCheck = customtkinter.CTkCheckBox(master=frame4, text="Admin", variable=admin_choice, onvalue=1, offvalue=0)
        self.adminCheck.pack(pady=5, padx=5, side=customtkinter.LEFT)
        
        self.operationButton = customtkinter.CTkButton(master=self.operationsFrame, text="Add user to DataBase", font=("Rockwell", 15), command=self.__addButtonClick)
        self.operationButton.pack(pady=5, padx=10, side=customtkinter.LEFT)
        
    def __addButtonClick(self):
        self.adminPresenter.addUser()
        
    def __createFindUserFrame(self):
        self.adminPresenter.resetFrame(self.operationsFrame.winfo_children())
        
        titleFind = customtkinter.CTkLabel(master=self.operationsFrame, text="Find user(s) by a specified criteria", font=("Rockwell", 20))
        titleFind.pack(pady=12, padx=10, side=customtkinter.TOP, anchor="w")
        
        frame1 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame1.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame1, text="Choose criteria: ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        search_criteria = ["ID", "Username", "Admin"]
        self.searchCriteriaCombo = customtkinter.CTkComboBox(master=frame1, values=search_criteria, state='readonly')
        self.searchCriteriaCombo.set("ID")
        self.searchCriteriaCombo.pack(pady=10, padx=10, side=customtkinter.LEFT, anchor="w")
        self.criteriaEntry = customtkinter.CTkEntry(master=frame1)
        self.criteriaEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        self.operationButton = customtkinter.CTkButton(master=self.operationsFrame, text="Search for user", font=("Rockwell", 15), command=self.__searchUserClick)
        self.operationButton.pack(pady=5, padx=10, side=customtkinter.TOP, anchor="w")
        
        self.showFrame = customtkinter.CTkScrollableFrame(master=self.operationsFrame)
        self.showFrame.pack(pady=20, padx=20, fill="both", expand=True)
        
    def __searchUserClick(self):
        self.adminPresenter.resetFrame(self.showFrame.winfo_children())
        self.adminPresenter.findUsersBy()
        
    def __createUpdateUserFrame(self):
        self.adminPresenter.resetFrame(self.operationsFrame.winfo_children())
        
        titleAdd = customtkinter.CTkLabel(master=self.operationsFrame, text="Select a user and update its information", font=("Rockwell", 20))
        titleAdd.pack(pady=12, padx=10, side=customtkinter.TOP, anchor="w")
        
        frame1 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame1.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame1, text="Select a user ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.adminPresenter.findAllUsers(frame1)
        
        frame2 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame2.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame2, text="Select which field to update ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        search_criteria = ["ID", "Username", "Password"]
        self.searchCriteriaCombo = customtkinter.CTkComboBox(master=frame2, values=search_criteria, state='readonly')
        self.searchCriteriaCombo.set("ID")
        self.searchCriteriaCombo.pack(pady=10, padx=10, side=customtkinter.LEFT, anchor="w")
        
        frame3 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame3.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame3, text="Enter the value to be updated with: ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.criteriaEntry = customtkinter.CTkEntry(master=frame3)
        self.criteriaEntry.pack(pady=0, padx=10, side=customtkinter.LEFT, anchor="w")
        
        frame4 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame4.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame4, text="Make this user an admin? ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.adminChoice = customtkinter.IntVar()
        self.radio_yes = customtkinter.CTkRadioButton(master=frame4, text="Yes", variable=self.adminChoice, value=1)
        self.radio_yes.pack(pady=5, padx=5, side=customtkinter.LEFT)
        self.radio_no = customtkinter.CTkRadioButton(master=frame4, text="No", variable=self.adminChoice, value=0)
        self.radio_no.pack(pady=5, padx=5, side=customtkinter.LEFT)
        
        self.operationButton = customtkinter.CTkButton(master=self.operationsFrame, text="Update user information", font=("Rockwell", 15), command=self.__updateButtonClick)
        self.operationButton.pack(pady=5, padx=10, side=customtkinter.LEFT)
        
    def __updateButtonClick(self):
        self.adminPresenter.updateUser()
        
    def __createDeleteUserFrame(self):
        self.adminPresenter.resetFrame(self.operationsFrame.winfo_children())
        
        titleAdd = customtkinter.CTkLabel(master=self.operationsFrame, text="Select a user to delete from database", font=("Rockwell", 20))
        titleAdd.pack(pady=12, padx=10, side=customtkinter.TOP, anchor="w")
        
        frame1 = customtkinter.CTkFrame(master=self.operationsFrame)
        frame1.pack(pady=5, padx=10, fill="both", expand=False)
        criteriaLabel = customtkinter.CTkLabel(master=frame1, text="Select a user ", font=("Rockwell", 15))
        criteriaLabel.pack(pady=12, padx=10, side=customtkinter.LEFT, anchor="w")
        self.adminPresenter.findAllUsers(frame1)
        
        self.operationButton = customtkinter.CTkButton(master=self.operationsFrame, text="Delete user", font=("Rockwell", 15), command=self.__deleteButtonClick)
        self.operationButton.pack(pady=5, padx=10, side=customtkinter.LEFT)
    
    def __deleteButtonClick(self):
        self.adminPresenter.deleteUser()
        
    def __createSeeAllUsersFrame(self):
        self.adminPresenter.resetFrame(self.operationsFrame.winfo_children())
        
        titleAdd = customtkinter.CTkLabel(master=self.operationsFrame, text="All the users of the zoo are: ", font=("Rockwell", 20))
        titleAdd.pack(pady=12, padx=10, side=customtkinter.TOP, anchor="w")
        
        self.showFrame = customtkinter.CTkScrollableFrame(master=self.operationsFrame)
        self.showFrame.pack(pady=20, padx=20, fill="both", expand=True)
        
        self.adminPresenter.printAllUsers()
        
    #interface methods implementation
    def getIdEntry(self):
        return self.idEntry.get()

    def getUsernameEntry(self):
        return self.usernameEntry.get()
    
    def getPasswordEntry(self):
        return self.passwordEntry.get()
    
    def getAdminCheckBox(self):
        return self.adminCheck.get()
    
    def setErrorMessage(self, titlu, message):
        messagebox.showerror(titlu, message)
        
    def setInfoMessage(self, titlu, message):
        messagebox.showinfo(titlu, message)
    
    def getSearchCriteriaCombo(self):
        return self.searchCriteriaCombo.get()
    
    def getCriteriaEntry(self):
        return self.criteriaEntry.get()
    
    def getSearchUserCombo(self):
        return self.searchUserCombo.get()
    
    def setShowFrame(self, label_text):
        labelF = customtkinter.CTkLabel(master=self.showFrame, text=label_text, font=("Rockwell", 14))
        labelF.pack(pady=0, padx=20, side=customtkinter.TOP, anchor="w")
        
    def setSearchUserCombo(self, values, frame1):
        self.searchUserCombo = customtkinter.CTkComboBox(master=frame1, values=values, state='readonly')
        self.searchUserCombo.set(values[0])
        self.searchUserCombo.pack(pady=10, padx=10, side=customtkinter.LEFT, anchor="w")
        
    def getAdminChoice(self):
        return self.adminChoice.get()