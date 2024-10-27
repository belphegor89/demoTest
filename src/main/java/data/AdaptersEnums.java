package data;

public class AdaptersEnums{
    public enum adaptersTableColumns{
        NAME,
        SETTING_COUNT,
        ACTION,
        SETTING_TYPE,
        OS,
        TRAFFIC,
        AD_FORMATS,
        SIZES,
        COUNTRIES,
        EXPAND;

        public String publicName(){
            return switch (this){
                case NAME -> "Name";
                case SETTING_COUNT -> "Setting Count";
                case ACTION -> "Action";
                case SETTING_TYPE -> "Setting Type";
                case OS -> "OS";
                case TRAFFIC -> "Traffic";
                case AD_FORMATS -> "Ad Formats";
                case SIZES -> "Sizes";
                case COUNTRIES -> "Countries";
                case EXPAND -> "Expand";
            };
        }

        public String attributeName(){
            return switch (this){
                case NAME -> "name";
                case SETTING_COUNT -> "settings_count";
                case ACTION -> "action";
                case SETTING_TYPE -> "type";
                case OS -> "os";
                case TRAFFIC -> "traffic";
                case AD_FORMATS -> "ad_format";
                case SIZES -> "sizes";
                case COUNTRIES -> "countries";
                case EXPAND -> "expand";
            };
        }

    }

    public enum adaptersApiEnum{
        SPOTX("SpotX"),
        MEDIAGRID("MediaGrid"),
        E_PLANNING("E-Planning"),
        VERIZON("Verizon"),
        XANDR("Xandr"),
        BIDSWITCH("Bidswitch"),
        INMOBI("InMobi"),
        PUBMATIC("Pubmatic"),
        MAGNITE("Magnite"),
        EPSILON("Epsilon"),
        OPENX("OpenX"),
        IMPROVEDIGITAL("ImproveDigital"),
        RICHAUDIENCE("RichAudience"),
        SYNACOR("Synacor"),
        SMARTADSERVER("SmartAdserver"),
        YAHOO("Yahoo"),
        ADFORM("Adform"),
        SOVRN("Sovrn"),
        CRITEO("Criteo"),
        SHARETHROUGH("Sharethrough"),
        ANIVIEW("Aniview"),
        UNRULY("Unruly"),
        ACROSS_33("33Across"),
        TRADEDESK("TradeDesk"),
        ADVIEW("AdView"),
        PUBWISE("PubWise"),
        FREEWHEEL("Freewheel"),
        MAGNITECTV("MagniteCTV"),
        ADTONOS("AdTonos");

        private final String name;

        adaptersApiEnum(String name){
            this.name = name;
        }

        public String publicName(){
            return name;
        }

        public static adaptersApiEnum getByPublicName(String name){
            for(adaptersApiEnum adapter : adaptersApiEnum.values()){
                if(adapter.publicName().equalsIgnoreCase(name)){
                    return adapter;
                }
            }
            return null;
        }
    }

    public enum adaptersPrebidEnum{
        MEDIAGRID("TheMediaGrid-Prebid"),
        ACROSS_33("33Across-Prebid"),
        ADFORM("Adform-Prebid"),
        ADGENERATION("AdGeneration-Prebid"),
        ADHESE("Adhese Adapter Parameters-Prebid"),
        ADKERNEL("Adkernel-Prebid"),
        ADKERNELADN("AdkernelAdn-Prebid"),
        ADMAN("Adman-Prebid"),
        ADMIXER("Admixer-Prebid"),
        ADOCEAN("AdOcean-Prebid"),
        ADOPPLER("Adoppler-Prebid"),
        ADPONE("Adpone-Prebid"),
        ADTARGET("Adtarget-Prebid"),
        ADTELLIGENT("Adtelligent-Prebid"),
        ADVANGELISTS("Advangelists-Prebid"),
        AJA("AJA-Prebid"),
        APPLOGY("Applogy-Prebid"),
        APPNEXUS("Appnexus-Prebid"),
        FACEBOOK("Facebook Audience Network-Prebid"),
        AVOCET("Avocet-Prebid"),
        BEACHFRONT("Beachfront-Prebid"),
        BEINTOO("Beintoo-Prebid"),
        BRIGHTROLL("Brightroll-Prebid"),
        CONSUMABLE("Consumable-Prebid"),
        CONVERSANT("Conversant-Prebid"),
        CPMSTAR("Cpmstar-Prebid"),
        DATABLOCKS("Datablocks-Prebid"),
        DISTRICT_M_DMX("District M DMX-Prebid"),
        EMX_DIGITAL("EMX Digital-Prebid"),
        ENGAGEBDR("EngageBDR-Prebid"),
        EPLANNING("EPlanning-Prebid"),
        GAMMA("Gamma-Prebid"),
        GAMOSHI("Gamoshi-Prebid"),
        GUMGUM("GumGum-Prebid"),
        IMPROVE_DIGITAL("Improve Digital-Prebid"),
        IX("Ix-Prebid"),
        KIDOZ("Kidoz-Prebid"),
        KUBIENT("Kubient-Prebid"),
        LIFESTREET("Lifestreet-Prebid"),
        LOCKERDOME("LockerDome-Prebid"),
        LOGICAD("Logicad-Prebid"),
        LUNAMEDIA("LunaMedia-Prebid"),
        MARSMEDIA("Marsmedia-Prebid"),
        MGID("Mgid-Prebid"),
        MOBILEFUSE("MobileFuse-Prebid"),
        NANOINTERACTIVE("NanoInteractive-Prebid"),
        NINTHDECIMAL("NinthDecimal-Prebid"),
        OPENX("Openx-Prebid"),
        ORBIDDER("Orbidder-Prebid"),
        PUBMATIC("Pubmatic-Prebid"),
        PUBNATIVE("Pubnative-Prebid"),
        PULSEPOINT("Pulsepoint-Prebid"),
        RHYTHMONE("Rhythmone-Prebid"),
        RTB_HOUSE("RTB House-Prebid"),
        RUBICON("Rubicon-Prebid"),
        SHARETHROUGH("Sharethrough-Prebid"),
        SMARTADSERVER("Smartadserver-Prebid"),
        SMARTRTB("SmartRTB-Prebid"),
        SOMOAUDIENCE("SomoAudience-Prebid"),
        SONOBI("Sonobi-Prebid"),
        SOVRN("Sovrn-Prebid"),
        SYNACORMEDIA("Synacormedia-Prebid"),
        TAPPX("Tappx-Prebid"),
        TELARIA("Telaria-Prebid"),
        TRIPLELIFT("Triplelift-Prebid"),
        TRIPLELIFT_NATIVE("triplelift_native-Prebid"),
        UCFUNNEL("Ucfunnel-Prebid"),
        UNRULY("Unruly-Prebid"),
        VALUEIMPRESSION("ValueImpression-Prebid"),
        VERIZONMEDIA("VerizonMedia-Prebid"),
        VIS_X("VIS.X-Prebid"),
        VRTCAL("Vrtcal-Prebid"),
        YEAHMOBI("Yeahmobi-Prebid"),
        YIELDLAB("Yieldlab-Prebid"),
        YIELDMO("Yieldmo-Prebid"),
        YIELDONE("Yieldone-Prebid"),
        ZEROCLICKFRAUD("ZeroClickFraud-Prebid"),
        ADYOULIKE("AdYouLike-Prebid"),
        INMOBI("InMobi-Prebid"),
        SMILEWANTED("SmileWanted-Prebid"),
        OUTBRAIN("Outbrain-Prebid"),
        FREEWHEELSSP("FreeWheelssp-Prebid");

        private final String name;

        adaptersPrebidEnum(String name){
            this.name = name;
        }

        public String publicName(){
            return name;
        }

        public static adaptersPrebidEnum getByPublicName(String name){
            for(adaptersPrebidEnum adapter : adaptersPrebidEnum.values()){
                if(adapter.publicName().equalsIgnoreCase(name)){
                    return adapter;
                }
            }
            return null;
        }
    }

}
