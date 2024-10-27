package data;

public class InventoryEnum {

    public enum inventoryTypeEnum {
        ANDROID,
        IOS,
        WEB,
        CTV;

        public String attributeName() {
            return switch (this) {
                case ANDROID -> "android";
                case IOS -> "ios";
                case WEB -> "web";
                case CTV -> "ctv";
            };
        }

        public String publicName() {
            return switch (this) {
                case ANDROID -> "Android App";
                case IOS -> "iOS App";
                case WEB -> "Website";
                case CTV -> "CTV";
            };
        }
    }

    public enum manageInventoryStatusEnum {
        ALL,
        APPROVED,
        DECLINED,
        PENDING_APPROVAL;

        public String publicName() {
            return switch (this) {
                case ALL -> "All";
                case APPROVED -> "Approved";
                case DECLINED -> "Declined";
                case PENDING_APPROVAL -> "Pending Approval";
            };
        }
    }

    public enum manageInventoryPerPageEnum {
        FIFTEEN,
        FIFTY,
        ONE_HUNDRED,
        FIVE_HUNDRED,
        ONE_THOUSAND;

        public String publicName() {
            return switch (this) {
                case FIFTEEN -> "15";
                case FIFTY -> "50";
                case ONE_HUNDRED -> "100";
                case FIVE_HUNDRED -> "500";
                case ONE_THOUSAND -> "1000";
            };
        }
    }

    public enum inventoryStatusEnum {
        ACTIVE,
        DECLINED,
        PENDING;

        public String publicName() {
            return switch (this) {
                case ACTIVE -> "Active";
                case DECLINED -> "Declined";
                case PENDING -> "Pending";
            };
        }
    }

    public enum adCategoryEnum {
        IAB1, IAB1_1, IAB1_2, IAB1_3, IAB1_4, IAB1_5, IAB1_6, IAB1_7, IAB2, IAB2_1, IAB2_2, IAB2_3, IAB2_4, IAB2_5, IAB2_6, IAB2_7, IAB2_8, IAB2_9, IAB2_10, IAB2_11, IAB2_12, IAB2_13, IAB2_14, IAB2_15, IAB2_16, IAB2_17, IAB2_18, IAB2_19, IAB2_20, IAB2_21, IAB2_22, IAB2_23, IAB3, IAB3_1, IAB3_2, IAB3_3, IAB3_4, IAB3_5, IAB3_6, IAB3_7, IAB3_8, IAB3_9, IAB3_10, IAB3_11, IAB3_12, IAB4, IAB4_1, IAB4_2, IAB4_3, IAB4_4, IAB4_5, IAB4_6, IAB4_7, IAB4_8, IAB4_9, IAB4_10, IAB4_11, IAB5, IAB5_1, IAB5_2, IAB5_3, IAB5_4, IAB5_5, IAB5_6, IAB5_7, IAB5_8, IAB5_9, IAB5_10, IAB5_11, IAB5_12, IAB5_13, IAB5_14, IAB5_15, IAB6, IAB6_1, IAB6_2, IAB6_3, IAB6_4, IAB6_5, IAB6_6, IAB6_7, IAB6_8, IAB6_9, IAB7, IAB7_1, IAB7_2, IAB7_3, IAB7_4, IAB7_5, IAB7_6, IAB7_7, IAB7_8, IAB7_9, IAB7_10, IAB7_11, IAB7_12, IAB7_13, IAB7_14, IAB7_15, IAB7_16, IAB7_17, IAB7_18, IAB7_19, IAB7_20, IAB7_21, IAB7_22, IAB7_23, IAB7_24, IAB7_25, IAB7_26, IAB7_27, IAB7_28, IAB7_29, IAB7_30, IAB7_31, IAB7_32, IAB7_33, IAB7_34, IAB7_35, IAB7_36, IAB7_37, IAB7_38, IAB7_39, IAB7_40, IAB7_41, IAB7_42, IAB7_43, IAB7_44, IAB7_45, IAB8, IAB8_1, IAB8_2, IAB8_3, IAB8_4, IAB8_5, IAB8_6, IAB8_7, IAB8_8, IAB8_9, IAB8_10, IAB8_11, IAB8_12, IAB8_13, IAB8_14, IAB8_15, IAB8_16, IAB8_17, IAB8_18, IAB9, IAB9_1, IAB9_2, IAB9_3, IAB9_4, IAB9_5, IAB9_6, IAB9_7, IAB9_8, IAB9_9, IAB9_10, IAB9_11, IAB9_12, IAB9_13, IAB9_14, IAB9_15, IAB9_16, IAB9_17, IAB9_18, IAB9_19, IAB9_20, IAB9_21, IAB9_22, IAB9_23, IAB9_24, IAB9_25, IAB9_26, IAB9_27, IAB9_28, IAB9_29, IAB9_30, IAB9_31, IAB10, IAB10_1, IAB10_2, IAB10_3, IAB10_4, IAB10_5, IAB10_6, IAB10_7, IAB10_8, IAB10_9, IAB11, IAB11_1, IAB11_2, IAB11_3, IAB11_4, IAB11_5, IAB12, IAB12_1, IAB12_2, IAB12_3, IAB13, IAB13_1, IAB13_2, IAB13_3, IAB13_4, IAB13_5, IAB13_6, IAB13_7, IAB13_8, IAB13_9, IAB13_10, IAB13_11, IAB13_12, IAB14, IAB14_1, IAB14_2, IAB14_3, IAB14_4, IAB14_5, IAB14_6, IAB14_7, IAB14_8, IAB15, IAB15_1, IAB15_2, IAB15_3, IAB15_4, IAB15_5, IAB15_6, IAB15_7, IAB15_8, IAB15_9, IAB15_10, IAB16, IAB16_1, IAB16_2, IAB16_3, IAB16_4, IAB16_5, IAB16_6, IAB16_7, IAB17, IAB17_1, IAB17_2, IAB17_3, IAB17_4, IAB17_5, IAB17_6, IAB17_7, IAB17_8, IAB17_9, IAB17_10, IAB17_11, IAB17_12, IAB17_13, IAB17_14, IAB17_15, IAB17_16, IAB17_17, IAB17_18, IAB17_19, IAB17_20, IAB17_21, IAB17_22, IAB17_23, IAB17_24, IAB17_25, IAB17_26, IAB17_27, IAB17_28, IAB17_29, IAB17_30, IAB17_31, IAB17_32, IAB17_33, IAB17_34, IAB17_35, IAB17_36, IAB17_37, IAB17_38, IAB17_39, IAB17_40, IAB17_41, IAB17_42, IAB17_43, IAB17_44, IAB18, IAB18_1, IAB18_2, IAB18_3, IAB18_4, IAB18_5, IAB18_6, IAB19, IAB19_1, IAB19_2, IAB19_3, IAB19_4, IAB19_5, IAB19_6, IAB19_7, IAB19_8, IAB19_9, IAB19_10, IAB19_11, IAB19_12, IAB19_13, IAB19_14, IAB19_15, IAB19_16, IAB19_17, IAB19_18, IAB19_19, IAB19_20, IAB19_21, IAB19_22, IAB19_23, IAB19_24, IAB19_25, IAB19_26, IAB19_27, IAB19_28, IAB19_29, IAB19_30, IAB19_31, IAB19_32, IAB19_33, IAB19_34, IAB19_35, IAB19_36, IAB20, IAB20_1, IAB20_2, IAB20_3, IAB20_4, IAB20_5, IAB20_6, IAB20_7, IAB20_8, IAB20_9, IAB20_10, IAB20_11, IAB20_12, IAB20_13, IAB20_14, IAB20_15, IAB20_16, IAB20_17, IAB20_18, IAB20_19, IAB20_20, IAB20_21, IAB20_22, IAB20_23, IAB20_24, IAB20_25, IAB20_26, IAB20_27, IAB21, IAB21_1, IAB21_2, IAB21_3, IAB22, IAB22_1, IAB22_2, IAB22_3, IAB22_4, IAB23, IAB23_1, IAB23_2, IAB23_3, IAB23_4, IAB23_5, IAB23_6, IAB23_7, IAB23_8, IAB23_9, IAB23_10, IAB24, IAB25, IAB25_1, IAB25_2, IAB25_3, IAB25_4, IAB25_6, IAB25_7, IAB26, IAB26_1, IAB26_2, IAB26_3, IAB26_4;

        public String publicName() {
            return switch (this) {
                case IAB1 -> "IAB1 Arts & Entertainment";
                case IAB1_1 -> "IAB1-1 Books & Literature";
                case IAB1_2 -> "IAB1-2 Celebrity Fan/Gossip";
                case IAB1_3 -> "IAB1-3 Fine Art";
                case IAB1_4 -> "IAB1-4 Humor";
                case IAB1_5 -> "IAB1-5 Movies";
                case IAB1_6 -> "IAB1-6 Music";
                //                case IAB1_6 -> "IAB1-6 Music & Audio";
                case IAB1_7 -> "IAB1-7 Television";
                //                case IAB1_7 -> "IAB1-7 Television & Video";

                case IAB2 -> "IAB2 Automotive";
                case IAB2_1 -> "IAB2-1 Auto Parts";
                case IAB2_2 -> "IAB2-2 Auto Repair";
                case IAB2_3 -> "IAB2-3 Buying/Selling Cars";
                case IAB2_4 -> "IAB2-4 Car Culture";
                case IAB2_5 -> "IAB2-5 Certified Pre-Owned";
                case IAB2_6 -> "IAB2-6 Convertible";
                case IAB2_7 -> "IAB2-7 Coupe";
                case IAB2_8 -> "IAB2-8 Crossover";
                //                case IAB2_9 -> "IAB2-9 Diesel";
                case IAB2_9 -> "IAB2-9 Diese";
                case IAB2_10 -> "IAB2-10 Electric Vehicle";
                case IAB2_11 -> "IAB2-11 Hatchback";
                case IAB2_12 -> "IAB2-12 Hybrid";
                case IAB2_13 -> "IAB2-13 Luxury";
                case IAB2_14 -> "IAB2-14 MiniVan";
                case IAB2_15 -> "IAB2-15 Motorcycles";
                case IAB2_16 -> "IAB2-16 Off-Road Vehicles";
                case IAB2_17 -> "IAB2-17 Performance Vehicles";
                case IAB2_18 -> "IAB2-18 Pickup";
                case IAB2_19 -> "IAB2-19 Road-Side Assistance";
                case IAB2_20 -> "IAB2-20 Sedan";
                case IAB2_21 -> "IAB2-21 Trucks & Accessories";
                case IAB2_22 -> "IAB2-22 Vintage Cars";
                case IAB2_23 -> "IAB2-23 Wagon";

                case IAB3 -> "IAB3 Business";
                case IAB3_1 -> "IAB3-1 Advertising";
                case IAB3_2 -> "IAB3-2 Agriculture";
                case IAB3_3 -> "IAB3-3 Biotech/Biomedical";
                case IAB3_4 -> "IAB3-4 Business Software";
                case IAB3_5 -> "IAB3-5 Construction";
                case IAB3_6 -> "IAB3-6 Forestry";
                case IAB3_7 -> "IAB3-7 Government";
                case IAB3_8 -> "IAB3-8 Green Solutions";
                case IAB3_9 -> "IAB3-9 Human Resources";
                case IAB3_10 -> "IAB3-10 Logistics";
                case IAB3_11 -> "IAB3-11 Marketing";
                case IAB3_12 -> "IAB3-12 Metals";

                case IAB4 -> "IAB4 Careers";
                case IAB4_1 -> "IAB4-1 Career Planning";
                case IAB4_2 -> "IAB4-2 College";
                case IAB4_3 -> "IAB4-3 Financial Aid";
                case IAB4_4 -> "IAB4-4 Job Fairs";
                case IAB4_5 -> "IAB4-5 Job Search";
                case IAB4_6 -> "IAB4-6 Resume Writing/Advice";
                case IAB4_7 -> "IAB4-7 Nursing";
                case IAB4_8 -> "IAB4-8 Scholarships";
                case IAB4_9 -> "IAB4-9 Telecommuting";
                case IAB4_10 -> "IAB4-10 U.S. Military";
                //                case IAB4_10 -> "IAB4-10 Military";
                case IAB4_11 -> "IAB4-11 Career Advice";

                case IAB5 -> "IAB5 Education";
                case IAB5_1 -> "IAB5-1 7-12 Education";
                case IAB5_2 -> "IAB5-2 Adult Education";
                case IAB5_3 -> "IAB5-3 Art History";
                case IAB5_4 -> "IAB5-4 Colledge Administration";
                //                case IAB5_4 -> "IAB5-4 College Administration";
                case IAB5_5 -> "IAB5-5 College Life";
                case IAB5_6 -> "IAB5-6 Distance Learning";
                case IAB5_7 -> "IAB5-7 English as a 2nd Language";
                case IAB5_8 -> "IAB5-8 Language Learning";
                case IAB5_9 -> "IAB5-9 Graduate School";
                case IAB5_10 -> "IAB5-10 Homeschooling";
                case IAB5_11 -> "IAB5-11 Homework/Study Tips";
                case IAB5_12 -> "IAB5-12 K-6 Educators";
                case IAB5_13 -> "IAB5-13 Private School";
                case IAB5_14 -> "IAB5-14 Special Education";
                case IAB5_15 -> "IAB5-15 Studying Business";

                case IAB6 -> "IAB6 Family";
                case IAB6_1 -> "IAB6-1 Adoption";
                case IAB6_2 -> "IAB6-2 Babies & Toddlers";
                case IAB6_3 -> "IAB6-3 Daycare/Pre School";
                case IAB6_4 -> "IAB6-4 Family Internet";
                case IAB6_5 -> "IAB6-5 Parenting - K-6 Kids";
                case IAB6_6 -> "IAB6-6 Parenting teens";
                case IAB6_7 -> "IAB6-7 Pregnancy";
                case IAB6_8 -> "IAB6-8 Special Needs Kids";
                case IAB6_9 -> "IAB6-9 Eldercare";

                case IAB7 -> "IAB7 Health & Fitness";
                case IAB7_1 -> "IAB7-1 Exercise";
                case IAB7_2 -> "IAB7-2 A.D.D.";
                case IAB7_3 -> "IAB7-3 AIDS/HIV";
                case IAB7_4 -> "IAB7-4 Allergies";
                case IAB7_5 -> "IAB7-5 Alternative Medicine";
                case IAB7_6 -> "IAB7-6 Arthritis";
                case IAB7_7 -> "IAB7-7 Asthma";
                case IAB7_8 -> "IAB7-8 Autism/PDD";
                case IAB7_9 -> "IAB7-9 Bipolar Disorder";
                case IAB7_10 -> "IAB7-10 Brain Tumor";
                case IAB7_11 -> "IAB7-11 Cancer";
                case IAB7_12 -> "IAB7-12 Cholesterol";
                case IAB7_13 -> "IAB7-13 Chronic Fatigue Syndrome";
                case IAB7_14 -> "IAB7-14 Chronic Pain";
                case IAB7_15 -> "IAB7-15 Cold & Flu";
                case IAB7_16 -> "IAB7-16 Deafness";
                case IAB7_17 -> "IAB7-17 Dental Care";
                case IAB7_18 -> "IAB7-18 Depression";
                case IAB7_19 -> "IAB7-19 Dermatology";
                case IAB7_20 -> "IAB7-20 Diabetes";
                case IAB7_21 -> "IAB7-21 Epilepsy";
                case IAB7_22 -> "IAB7-22 GERD/Acid Reflux";
                case IAB7_23 -> "IAB7-23 Headaches/Migraines";
                case IAB7_24 -> "IAB7-24 Heart Disease";
                case IAB7_25 -> "IAB7-25 Herbs for Health";
                case IAB7_26 -> "IAB7-26 Holistic Healing";
                case IAB7_27 -> "IAB7-27 IBS/Crohn's Disease";
                case IAB7_28 -> "IAB7-28 Incest/Abuse Support";
                case IAB7_29 -> "IAB7-29 Incontinence";
                case IAB7_30 -> "IAB7-30 Infertility";
                case IAB7_31 -> "IAB7-31 Men's Health";
                case IAB7_32 -> "IAB7-32 Nutrition";
                case IAB7_33 -> "IAB7-33 Orthopedics";
                case IAB7_34 -> "IAB7-34 Panic/Anxiety Disorders";
                case IAB7_35 -> "IAB7-35 Pediatrics";
                case IAB7_36 -> "IAB7-36 Physical Therapy";
                case IAB7_37 -> "IAB7-37 Psychology/Psychiatry";
                case IAB7_38 -> "IAB7-38 Senor Health";
                //                case IAB7_38 -> "IAB7-38 Senior Health";
                case IAB7_39 -> "IAB7-39 Sexuality";
                case IAB7_40 -> "IAB7-40 Sleep Disorders";
                case IAB7_41 -> "IAB7-41 Smoking Cessation";
                case IAB7_42 -> "IAB7-42 Substance Abuse";
                case IAB7_43 -> "IAB7-43 Thyroid Disease";
                case IAB7_44 -> "IAB7-44 Weight Loss";
                case IAB7_45 -> "IAB7-45 Women's Health";

                case IAB8 -> "IAB8 Food & Drink";
                case IAB8_1 -> "IAB8-1 American Cuisine";
                case IAB8_2 -> "IAB8-2 Barbecues & Grilling";
                case IAB8_3 -> "IAB8-3 Cajun/Creole";
                case IAB8_4 -> "IAB8-4 Chinese Cuisine";
                case IAB8_5 -> "IAB8-5 Cocktails/Beer";
                case IAB8_6 -> "IAB8-6 Coffee/Tea";
                case IAB8_7 -> "IAB8-7 Cuisine-Specific";
                case IAB8_8 -> "IAB8-8 Desserts & Baking";
                case IAB8_9 -> "IAB8-9 Dining Out";
                case IAB8_10 -> "IAB8-10 Food Allergies";
                case IAB8_11 -> "IAB8-11 French Cuisine";
                case IAB8_12 -> "IAB8-12 Health/Lowfat Cooking";
                case IAB8_13 -> "IAB8-13 Italian Cuisine";
                case IAB8_14 -> "IAB8-14 Japanese Cuisine";
                case IAB8_15 -> "IAB8-15 Mexican Cuisine";
                case IAB8_16 -> "IAB8-16 Vegan";
                case IAB8_17 -> "IAB8-17 Vegetarian";
                case IAB8_18 -> "IAB8-18 Wine";

                case IAB9 -> "IAB9 Hobbies & Interests";
                case IAB9_1 -> "IAB9-1 Art/Technology";
                case IAB9_2 -> "IAB9-2 Arts & Crafts";
                case IAB9_3 -> "IAB9-3 Beadwork";
                case IAB9_4 -> "IAB9-4 Birdwatching";
                //                case IAB9_4 -> "IAB9-4 Bird-Watching";
                case IAB9_5 -> "IAB9-5 Board Games/Puzzles";
                case IAB9_6 -> "IAB9-6 Candle & Soap Making";
                case IAB9_7 -> "IAB9-7 Card Games";
                case IAB9_8 -> "IAB9-8 Chess";
                case IAB9_9 -> "IAB9-9 Cigars";
                case IAB9_10 -> "IAB9-10 Collecting";
                case IAB9_11 -> "IAB9-11 Comic Books";
                case IAB9_12 -> "IAB9-12 Drawing/Sketching";
                case IAB9_13 -> "IAB9-13 Freelance Writing";
                case IAB9_14 -> "IAB9-14 Genealogy";
                case IAB9_15 -> "IAB9-15 Getting Published";
                case IAB9_16 -> "IAB9-16 Guitar";
                case IAB9_17 -> "IAB9-17 Home Recording";
                case IAB9_18 -> "IAB9-18 Investors & Patents";
                case IAB9_19 -> "IAB9-19 Jewelry Making";
                case IAB9_20 -> "IAB9-20 Magic & Illusion";
                case IAB9_21 -> "IAB9-21 Needlework";
                case IAB9_22 -> "IAB9-22 Painting";
                case IAB9_23 -> "IAB9-23 Photography";
                case IAB9_24 -> "IAB9-24 Radio";
                case IAB9_25 -> "IAB9-25 Roleplaying Games";
                case IAB9_26 -> "IAB9-26 Sci-Fi & Fantasy";
                case IAB9_27 -> "IAB9-27 Scrapbooking";
                case IAB9_28 -> "IAB9-28 Screenwriting";
                case IAB9_29 -> "IAB9-29 Stamps & Coins";
                case IAB9_30 -> "IAB9-30 Video & Computer Games";
                case IAB9_31 -> "IAB9-31 Woodworking";

                case IAB10 -> "IAB10 Home & Garden";
                case IAB10_1 -> "IAB10-1 Appliances";
                case IAB10_2 -> "IAB10-2 Entertaining";
                case IAB10_3 -> "IAB10-3 Environmental Safety";
                case IAB10_4 -> "IAB10-4 Gardening";
                case IAB10_5 -> "IAB10-5 Home Repair";
                case IAB10_6 -> "IAB10-6 Home Theater";
                case IAB10_7 -> "IAB10-7 Interior Decorating";
                case IAB10_8 -> "IAB10-8 Landscaping";
                case IAB10_9 -> "IAB10-9 Remodeling & Construction";

                case IAB11 -> "IAB11 Law, Gov't & Politics";
                case IAB11_1 -> "IAB11-1 Immigration";
                case IAB11_2 -> "IAB11-2 Legal Issues";
                case IAB11_3 -> "IAB11-3 U.S. Government Resources";
                case IAB11_4 -> "IAB11-4 Politics";
                case IAB11_5 -> "IAB11-5 Commentary";

                case IAB12 -> "IAB12 News";
                case IAB12_1 -> "IAB12-1 International News";
                case IAB12_2 -> "IAB12-2 National News";
                case IAB12_3 -> "IAB12-3 Local News";

                case IAB13 -> "IAB13 Personal Finance";
                case IAB13_1 -> "IAB13-1 Beginning Investing";
                case IAB13_2 -> "IAB13-2 Credit/Debt & Loans";
                case IAB13_3 -> "IAB13-3 Financial News";
                case IAB13_4 -> "IAB13-4 Financial Planning";
                case IAB13_5 -> "IAB13-5 Hedge Fund";
                case IAB13_6 -> "IAB13-6 Insurance";
                case IAB13_7 -> "IAB13-7 Investing";
                case IAB13_8 -> "IAB13-8 Mutual Funds";
                case IAB13_9 -> "IAB13-9 Options";
                case IAB13_10 -> "IAB13-10 Retirement Planning";
                case IAB13_11 -> "IAB13-11 Stocks";
                case IAB13_12 -> "IAB13-12 Tax Planning";

                case IAB14 -> "IAB14 Society";
                case IAB14_1 -> "IAB14-1 Dating";
                case IAB14_2 -> "IAB14-2 Divorce Support";
                case IAB14_3 -> "IAB14-3 Gay Life";
                case IAB14_4 -> "IAB14-4 Marriage";
                case IAB14_5 -> "IAB14-5 Senior Living";
                case IAB14_6 -> "IAB14-6 Teens";
                case IAB14_7 -> "IAB14-7 Weddings";
                case IAB14_8 -> "IAB14-8 Ethnic Specific";

                case IAB15 -> "IAB15 Science";
                case IAB15_1 -> "IAB15-1 Astrology";
                case IAB15_2 -> "IAB15-2 Biology";
                case IAB15_3 -> "IAB15-3 Chemistry";
                case IAB15_4 -> "IAB15-4 Geology";
                case IAB15_5 -> "IAB15-5 Paranormal Phenomena";
                case IAB15_6 -> "IAB15-6 Physics";
                case IAB15_7 -> "IAB15-7 Space/Astronomy";
                case IAB15_8 -> "IAB15-8 Ethnic Specific";
                //                case IAB15_8 -> "IAB15-8 Geography";
                case IAB15_9 -> "IAB15-9 Botany";
                case IAB15_10 -> "IAB15-10 Weather";

                case IAB16 -> "IAB16 Pets";
                case IAB16_1 -> "IAB16-1 Aquariums";
                case IAB16_2 -> "IAB16-2 Birds";
                case IAB16_3 -> "IAB16-3 Cats";
                case IAB16_4 -> "IAB16-4 Dogs";
                case IAB16_5 -> "IAB16-5 Large Animals";
                case IAB16_6 -> "IAB16-6 Reptiles";
                case IAB16_7 -> "IAB16-7 Veterinary Medicine";

                case IAB17 -> "IAB17 Sports";
                case IAB17_1 -> "IAB17-1 Auto Racing";
                case IAB17_2 -> "IAB17-2 Baseball";
                case IAB17_3 -> "IAB17-3 Bicycling";
                case IAB17_4 -> "IAB17-4 Bodybuilding";
                case IAB17_5 -> "IAB17-5 Boxing";
                case IAB17_6 -> "IAB17-6 Canoeing/Kayaking";
                case IAB17_7 -> "IAB17-7 Cheerleading";
                case IAB17_8 -> "IAB17-8 Climbing";
                case IAB17_9 -> "IAB17-9 Cricket";
                case IAB17_10 -> "IAB17-10 Figure Skating";
                case IAB17_11 -> "IAB17-11 Fly Fishing";
                case IAB17_12 -> "IAB17-12 Football";
                case IAB17_13 -> "IAB17-13 Freshwater Fishing";
                case IAB17_14 -> "IAB17-14 Game & Fish";
                case IAB17_15 -> "IAB17-15 Golf";
                case IAB17_16 -> "IAB17-16 Horse Racing";
                case IAB17_17 -> "IAB17-17 Horses";
                case IAB17_18 -> "IAB17-18 Hunting/Shooting";
                case IAB17_19 -> "IAB17-19 Inline Skating";
                case IAB17_20 -> "IAB17-20 Martial Arts";
                case IAB17_21 -> "IAB17-21 Mountain Biking";
                case IAB17_22 -> "IAB17-22 NASCAR Racing";
                case IAB17_23 -> "IAB17-23 Olympics";
                case IAB17_24 -> "IAB17-24 Paintball";
                case IAB17_25 -> "IAB17-25 Power & Motorcycles";
                case IAB17_26 -> "IAB17-26 Pro Basketball";
                case IAB17_27 -> "IAB17-27 Pro Ice Hockey";
                case IAB17_28 -> "IAB17-28 Rodeo";
                case IAB17_29 -> "IAB17-29 Rugby";
                case IAB17_30 -> "IAB17-30 Running/Jogging";
                case IAB17_31 -> "IAB17-31 Sailing";
                case IAB17_32 -> "IAB17-32 Saltwater Fishing";
                case IAB17_33 -> "IAB17-33 Scuba Diving";
                case IAB17_34 -> "IAB17-34 Skateboarding";
                case IAB17_35 -> "IAB17-35 Skiing";
                case IAB17_36 -> "IAB17-36 Snowboarding";
                case IAB17_37 -> "IAB17-37 Surfing/Bodyboarding";
                case IAB17_38 -> "IAB17-38 Swimming";
                case IAB17_39 -> "IAB17-39 Table Tennis/Ping-Pong";
                case IAB17_40 -> "IAB17-40 Tennis";
                case IAB17_41 -> "IAB17-41 Volleyball";
                case IAB17_42 -> "IAB17-42 Walking";
                case IAB17_43 -> "IAB17-43 Waterski/Wakeboard";
                case IAB17_44 -> "IAB17-44 World Soccer";

                case IAB18 -> "IAB18 Style & Fashion";
                case IAB18_1 -> "IAB18-1 Beauty";
                case IAB18_2 -> "IAB18-2 Body Art";
                case IAB18_3 -> "IAB18-3 Fashion";
                case IAB18_4 -> "IAB18-4 Jewelry";
                case IAB18_5 -> "IAB18-5 Clothing";
                case IAB18_6 -> "IAB18-6 Accessories";

                case IAB19 -> "IAB19 Technology & Computing";
                case IAB19_1 -> "IAB19-1 3-D Graphics";
                case IAB19_2 -> "IAB19-2 Animation";
                case IAB19_3 -> "IAB19-3 Antivirus Software";
                case IAB19_4 -> "IAB19-4 C/C++";
                case IAB19_5 -> "IAB19-5 Cameras & Camcorders";
                case IAB19_6 -> "IAB19-6 Cell Phones";
                case IAB19_7 -> "IAB19-7 Computer Certification";
                case IAB19_8 -> "IAB19-8 Computer Networking";
                case IAB19_9 -> "IAB19-9 Computer Peripherals";
                case IAB19_10 -> "IAB19-10 Computer Reviews";
                case IAB19_11 -> "IAB19-11 Data Centers";
                case IAB19_12 -> "IAB19-12 Databases";
                case IAB19_13 -> "IAB19-13 Desktop Publishing";
                case IAB19_14 -> "IAB19-14 Desktop Video";
                case IAB19_15 -> "IAB19-15 Email";
                case IAB19_16 -> "IAB19-16 Graphics Software";
                case IAB19_17 -> "IAB19-17 Home Video/DVD";
                case IAB19_18 -> "IAB19-18 Internet Technology";
                case IAB19_19 -> "Java";
                case IAB19_20 -> "IAB19-20 JavaScript";
                case IAB19_21 -> "IAB19-21 Mac Support";
                case IAB19_22 -> "IAB19-22 MP3/MIDI";
                case IAB19_23 -> "IAB19-23 Net Conferencing";
                case IAB19_24 -> "IAB19-24 Net for Beginners";
                case IAB19_25 -> "IAB19-25 Network Security";
                case IAB19_26 -> "IAB19-26 Palmtops/PDAs";
                case IAB19_27 -> "IAB19-27 PC Support";
                case IAB19_28 -> "IAB19-28 Portable";
                case IAB19_29 -> "IAB19-29 Entertainment";
                //                case IAB19_29 -> "IAB19-29 Virtual & Augmented Reality";
                case IAB19_30 -> "IAB19-30 Shareware/Freeware";
                case IAB19_31 -> "IAB19-31 Unix";
                case IAB19_32 -> "IAB19-32 Visual Basic";
                case IAB19_33 -> "IAB19-33 Web Clip Art";
                case IAB19_34 -> "IAB19-34 Web Design/HTML";
                case IAB19_35 -> "IAB19-35 Web Search";
                case IAB19_36 -> "IAB19-36 Windows";

                case IAB20 -> "IAB20 Travel";
                case IAB20_1 -> "IAB20-1 Adventure Travel";
                case IAB20_2 -> "IAB20-2 Africa";
                case IAB20_3 -> "IAB20-3 Air Travel";
                case IAB20_4 -> "IAB20-4 Australia & New Zealand";
                case IAB20_5 -> "IAB20-5 Bed & Breakfasts";
                case IAB20_6 -> "IAB20-6 Budget Travel";
                case IAB20_7 -> "IAB20-7 Business Travel";
                case IAB20_8 -> "IAB20-8 By US Locale";
                case IAB20_9 -> "IAB20-9 Camping";
                case IAB20_10 -> "IAB20-10 Canada";
                case IAB20_11 -> "IAB20-11 Caribbean";
                case IAB20_12 -> "IAB20-12 Cruises";
                case IAB20_13 -> "IAB20-13 Eastern Europe";
                case IAB20_14 -> "IAB20-14 Europe";
                case IAB20_15 -> "IAB20-15 France";
                case IAB20_16 -> "IAB20-16 Greece";
                case IAB20_17 -> "IAB20-17 Honeymoons/Getaways";
                case IAB20_18 -> "IAB20-18 Hotels";
                case IAB20_19 -> "IAB20-19 Italy";
                case IAB20_20 -> "IAB20-20 Japan";
                case IAB20_21 -> "IAB20-21 Mexico & Central America";
                case IAB20_22 -> "IAB20-22 National Parks";
                case IAB20_23 -> "IAB20-23 South America";
                case IAB20_24 -> "IAB20-24 Spas";
                case IAB20_25 -> "IAB20-25 Theme Parks";
                case IAB20_26 -> "IAB20-26 Traveling with Kids";
                case IAB20_27 -> "IAB20-27 United Kingdom";

                case IAB21 -> "IAB21 Real Estate";
                case IAB21_1 -> "IAB21-1 Apartments";
                case IAB21_2 -> "IAB21-2 Architects";
                case IAB21_3 -> "IAB21-3 Buying/Selling Homes";

                case IAB22 -> "IAB22 Shopping";
                case IAB22_1 -> "IAB22-1 Contests & Freebies";
                case IAB22_2 -> "IAB22-2 Couponing";
                case IAB22_3 -> "IAB22-3 Comparison";
                case IAB22_4 -> "IAB22-4 Engines";

                case IAB23 -> "IAB23 Religion & Spirituality";
                case IAB23_1 -> "IAB23-1 Alternative Religions";
                case IAB23_2 -> "IAB23-2 Atheism/Agnosticism";
                case IAB23_3 -> "IAB23-3 Buddhism";
                case IAB23_4 -> "IAB23-4 Catholicism";
                case IAB23_5 -> "IAB23-5 Christianity";
                case IAB23_6 -> "IAB23-6 Hinduism";
                case IAB23_7 -> "IAB23-7 Islam";
                case IAB23_8 -> "IAB23-8 Judaism";
                case IAB23_9 -> "IAB23-9 Latter-Day Saints";
                case IAB23_10 -> "IAB23-10 Pagan/Wiccan";

                case IAB24 -> "IAB24 Uncategorized";

                case IAB25 -> "IAB25 Non-Standard Content";
                case IAB25_1 -> "IAB25-1 Unmoderated UGC";
                case IAB25_2 -> "IAB25-2 Extreme Graphic/Explicit Violence";
                case IAB25_3 -> "IAB25-3 Pornography";
                //                case IAB25_3 -> "IAB25-3 Adult Content";
                case IAB25_4 -> "IAB25-4 Profane Content";
                //                case IAB25_5 -> "IAB25-5 Hate Content";
                case IAB25_6 -> "IAB25-6 Under Construction";
                case IAB25_7 -> "IAB25-7 Incentivized";

                case IAB26 -> "IAB26 Illegal Content";
                case IAB26_1 -> "IAB26-1 Illegal Content";
                case IAB26_2 -> "IAB26-2 Warez";
                case IAB26_3 -> "IAB26-3 Spyware/Malware";
                case IAB26_4 -> "IAB26-4 Copyright Infringement";

            };
        }
    }

}
