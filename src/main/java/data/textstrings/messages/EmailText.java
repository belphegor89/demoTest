package data.textstrings.messages;

public class EmailText {

    public static final String
            SIGNUP_SUBJECT_CONFIRMATION = "Confirmation of registration";


    //<editor-fold desc="User management / Authorization">
    public static final String
            SIGNUP_BY_USER_CONFIRMATION = "Hi ${firstName} ${lastName}!\n\nThank you for registering at ${platformUrl}. Before we can activate your account one last step must be taken.\n\nTo complete your registration, please click the button below:\n\n${confirmationUrl}\n\nIf you did not authorize or initiate this action, please contact our support team by replying to this email.\n\nWe appreciate your collaboration and trust, and hope you`ll enjoy using ${platformDomain}\n\nRegarding any questions or feedback, please feel free to contact us on ${supportName} (${supportMail})",
            SIGNUP_BY_ADMIN_CONFIRMATION = "Hi ${firstName} ${lastName}!\n\nYour account has been created at ${platformUrl}.\n\nTo complete your registration, please visit this URL:\n\n${confirmationUrl}\nIf you did not authorize this action, please contact our support team by replying to this email.\n\nRegarding any questions or feedback, contact us on ${adminName} (${supportMail})",
            SIGNUP_WELCOME = "Hey ${firstName} ${lastName}!\n\nWelcome aboard!\n\nBefore you start, we want to make sure that everything is set.\n\nFirst of all, you need to create an inventory and add placements.\n\nAfter doing that, you will be able to get our tag and paste it into your website HTML code.\n\nIt may take a couple of hours to approve your website, but we will do our best to make it as soon as we receive requests from your website or app.\n\nSo if you have any questions about the ${companyName} ${companyType} platform, feel free to ask us directly. We are glad to support you with anything you need to successfully grow your business.\n\nCheers,\n${companyName} ${companyType} Team";

    //</editor-fold>

    //<editor-fold desc="Inventory Manage">
    public static final String
            MULTIPLE_INVENTORIES_APPROVE_PLAIN = "\r\n\r\n\r\n\r\n    \r\n    \r\n    Notification: Inventory Approved - ${nameProject}\r\n\r\n\r\n\r\n\r\n    \r\n        \r\n            \r\n            \r\n                \r\n                \r\n                    Notification: Inventory Approved - ${nameProject}\r\n                \r\n                \r\n            \r\n            \r\n        \r\n    \r\n    \r\n        \r\n            \r\n            \r\n                \r\n                    &nbsp;\r\n                        Hi ${firstName} ${lastName}!\r\n    Yay, good news! Your inventory ${InventoryName1}, ${InventoryName2} were approved.\r\n    We appreciate your collaboration and trust in ${nameProject} ${typeProject}.\r\n    May you have any questions, please feel free to contact us on ${emailProject}.\r\n        We are always happy to answer all of your concerns and help you to grow your business.\r\n    \r\n\r\n                    &nbsp;\r\n                \r\n                \r\n            \r\n            \r\n        \r\n    \r\n    \r\n        \r\n            \r\n            \r\n                \r\n                \r\n                    All member work copyright of respective owner, otherwise ${currentYear} ${nameProject} ${typeProject}\r\n                \r\n                \r\n            \r\n            \r\n        \r\n    ",

    SINGLE_INVENTORY_APPROVE_PLAIN = "\r\n\r\n\r\n\r\n    \r\n    \r\n    Notification: Inventory Approved - ${nameProject}\r\n\r\n\r\n\r\n\r\n    \r\n        \r\n            \r\n            \r\n                \r\n                \r\n                    Notification: Inventory Approved - ${nameProject}\r\n                \r\n                \r\n            \r\n            \r\n        \r\n    \r\n    \r\n        \r\n            \r\n            \r\n                \r\n                    &nbsp;\r\n                        Hi ${firstName} ${lastName}!\r\n    Yay, good news! Your inventory ${InventoryName} was approved.\r\n    We appreciate your collaboration and trust in ${nameProject} ${typeProject}.\r\n    May you have any questions, please feel free to contact us on ${emailProject}.\r\n        We are always happy to answer all of your concerns and help you to grow your business.\r\n    \r\n\r\n                    &nbsp;\r\n                \r\n                \r\n            \r\n            \r\n        \r\n    \r\n    \r\n        \r\n            \r\n            \r\n                \r\n                \r\n                    All member work copyright of respective owner, otherwise ${currentYear} ${nameProject} ${typeProject}\r\n                \r\n                \r\n            \r\n            \r\n        \r\n    ";

}