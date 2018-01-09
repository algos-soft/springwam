package it.algos.springvaadin.enumeration;

import com.google.common.primitives.Longs;
import com.vaadin.server.Resource;
import com.vaadin.ui.Image;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by gac on 30 lug 2016.
 * Enum dei tipi di preferenza supportati
 */
public enum EAPrefType {

    string("stringa", EAFieldType.text) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof String) {
                String stringa = (String) obj;
                bytes = stringa.getBytes(Charset.forName("UTF-8"));
            }// end of if cycle
            return bytes;
        }// end of method

        @Override
        public String bytesToObject(byte[] bytes) {
            String obj = "";
            if (bytes != null) {
                obj = new String(bytes, Charset.forName("UTF-8"));
            }// end of if cycle
            return obj;
        }// end of method
    },// end of single enumeration

    bool("booleano", EAFieldType.checkbox) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof Boolean) {
                boolean bool = (boolean) obj;
                bytes = new byte[]{(byte) (bool ? 1 : 0)};
            }// end of if cycle
            return bytes;
        }// end of method

        @Override
        @SuppressWarnings("all")
        public Object bytesToObject(byte[] bytes) {
            Object obj = null;
            if (bytes.length > 0) {
                byte b = bytes[0];
                obj = new Boolean(b == (byte) 0b00000001);
            } else {
                obj = new Boolean(false);
            }// end of if/else cycle
            return obj;
        }// end of method
    },// end of single enumeration

    bool2("booleano", EAFieldType.checkboxlabel) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof Boolean) {
                boolean bool = (boolean) obj;
                bytes = new byte[]{(byte) (bool ? 1 : 0)};
            }// end of if cycle
            return bytes;
        }// end of method

        @Override
        @SuppressWarnings("all")
        public Object bytesToObject(byte[] bytes) {
            Object obj = null;
            if (bytes.length > 0) {
                byte b = bytes[0];
                obj = new Boolean(b == (byte) 0b00000001);
            } else {
                obj = new Boolean(false);
            }// end of if/else cycle
            return obj;
        }// end of method
    },// end of single enumeration

    integer("intero", EAFieldType.integer) {
        //@todo RIMETTERE

//        @Override
//        public byte[] objectToBytes(Object obj) {
//            byte[] bytes = new byte[0];
//            if (obj instanceof Integer) {
//                int num = (Integer) obj;
//                bytes = LibByte.intToByteArray(num);
//            }// end of if cycle
//            return bytes;
//        }// end of method
//
//        @Override
//        public Object bytesToObject(byte[] bytes) {
//            return LibByte.byteArrayToInt(bytes);
//        }// end of method
    },// end of single enumeration

    date("data", EAFieldType.localdatetime) {
        //@todo RIMETTERE

//        @Override
//        public byte[] objectToBytes(Object obj) {
//            byte[] bytes = new byte[0];
//            if (obj instanceof LocalDateTime) {
//                long millis = LibDate.getLongSecs((LocalDateTime) obj);
//                bytes = Longs.toByteArray(millis);
//            }// end of if cycle
//            return bytes;
//        }// end of method
//
//        @Override
//        public Object bytesToObject(byte[] bytes) {
//            return bytes.length > 0 ? LibDate.dateToLocalDateTime(new Date(Longs.fromByteArray(bytes))) : null;
//        }// end of method
    },// end of single enumeration

    email("email", EAFieldType.email) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof String) {
                String stringa = (String) obj;
                bytes = stringa.getBytes(Charset.forName("UTF-8"));
            }// end of if cycle
            return bytes;
        }// end of method

        @Override
        public Object bytesToObject(byte[] bytes) {
            Object obj = null;
            obj = new String(bytes, Charset.forName("UTF-8"));
            return obj;
        }// end of method
    },// end of single enumeration

//    decimal("decimale", AFieldType.lungo) {
//        @Override
//        public byte[] objectToBytes(Object obj) {
//            byte[] bytes = new byte[0];
//            if (obj instanceof BigDecimal) {
//                BigDecimal bd = (BigDecimal) obj;
//                bytes = LibByte.bigDecimalToByteArray(bd);
//            }// end of if cycle
//            return bytes;
//        }// end of method
//
//        @Override
//        public Object bytesToObject(byte[] bytes) {
//            return LibByte.byteArrayToBigDecimal(bytes);
//        }// end of method
//    },// end of single enumeration

    image("image", EAFieldType.image) {
        //@todo RIMETTERE

        //        @Override
//        public Object bytesToObject(byte[] bytes) {
//            Image img = null;
//            if (bytes.length > 0) {
//                img = LibImage.getImage(bytes);
//            }
//            return img;
//        }// end of method
    },// end of single enumeration

    resource("resource", EAFieldType.resource) {
        //@todo RIMETTERE

        //        @Override
//        public Object bytesToObject(byte[] bytes) {
//            Resource res = null;
//            Image img = null;
//            if (bytes.length > 0) {
//                img = LibImage.getImage(bytes);
//            }// end of if cycle
//            if (img != null) {
//                res = img.getSource();
//            }// end of if cycle
//            return res;
//        }// end of method
    },// end of single enumeration

    bytes("blog", EAFieldType.json);

    private String nome;
    private EAFieldType fieldType;


    EAPrefType(String nome, EAFieldType tipoDiFieldPerVisualizzareQuestoTipoDiPreferenza) {
        this.setNome(nome);
        this.setFieldType(tipoDiFieldPerVisualizzareQuestoTipoDiPreferenza);
    }// fine del costruttore


    /**
     * Converte un valore Object in ByteArray per questa preferenza.
     * Sovrascritto
     *
     * @param obj il valore Object
     *
     * @return il valore convertito in byte[]
     */
    public byte[] objectToBytes(Object obj) {
        return null;
    }// end of method


    /**
     * Converte un byte[] in Object del tipo adatto per questa preferenza.
     * Sovrascritto
     *
     * @param bytes il valore come byte[]
     *
     * @return il valore convertito nell'oggetto del tipo adeguato
     */
    public Object bytesToObject(byte[] bytes) {
        return null;
    }// end of method

    /**
     * Writes a value in the storage for this type of preference
     * Sovrascritto
     *
     * @param value the value
     */
    public void put(Object value) {
    }// end of method

    /**
     * Retrieves the value of this preference's type
     * Sovrascritto
     */
    public Object get() {
        return null;
    }// end of method


    public String getNome() {
        return nome;
    }// end of getter method

    public void setNome(String nome) {
        this.nome = nome;
    }//end of setter method

    public EAFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(EAFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public static String[] getValues() {
        String[] valori;
        EAPrefType[] types = values();
        valori = new String[values().length];

        for (int k = 0; k < types.length; k++) {
            valori[k] = types[k].toString();
        }// end of for cycle

        return valori;
    }// end of static method


}// end of enumeration class
