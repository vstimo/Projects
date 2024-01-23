library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity DecodificatorAscii is
    port(clk, rst: in std_logic;
         ascii:in std_logic_vector(7 downto 0);
         rgb:out std_logic;  --flag led rgb
         decod:out std_logic_vector(15 downto 0));
end DecodificatorAscii;

architecture Behavioral of DecodificatorAscii is
    signal decodSignal: std_logic_vector(15 downto 0);
    signal rgbSignal: std_logic:='0';
begin

    process(clk, rst)
    begin
        if rst = '1' then
            decodSignal <= (others => '0');
            rgbSignal <= '0';
        elsif rising_edge(clk) then
            case ascii is
                when x"01"=>rgbSignal<='1';--RGB
                when x"02"=>rgbSignal<='0';
                when x"03"=>decodSignal<=x"FFFF"; rgbSignal<='1';--APRINDERE TOATE
                when x"04"=>decodSignal<=x"0000"; rgbSignal<='0';  --STINGERE TOATE
                when x"FF"=>decodSignal<="XXXXXXXXXXXXXXXX"; rgbSignal<='0'; --ANIMATIE
                when x"00"=>decodSignal<=x"0000"; rgbSignal<='0';  --OPRESTE ANIMATIE 
                when x"05"=>decodSignal(0)<='1'; rgbSignal<='0';--L1 A
                when x"06"=>decodSignal(0)<='0'; rgbSignal<='0';--L1 S
                when x"07"=>decodSignal(1)<='1'; rgbSignal<='0';--L2 A
                when x"08"=>decodSignal(1)<='0'; rgbSignal<='0';--L2 S
                when x"09"=>decodSignal(2)<='1'; rgbSignal<='0';--L3 A
                when x"0A"=>decodSignal(2)<='0'; rgbSignal<='0';--L3 S
                when x"0B"=>decodSignal(3)<='1'; rgbSignal<='0';--L4 A
                when x"0C"=>decodSignal(3)<='0'; rgbSignal<='0';--L4 S
                when x"0D"=>decodSignal(4)<='1'; rgbSignal<='0';--L5 A
                when x"0E"=>decodSignal(4)<='0'; rgbSignal<='0';--L5 S
                when x"0F"=>decodSignal(5)<='1'; rgbSignal<='0';--L6 A
                when x"10"=>decodSignal(5)<='0'; rgbSignal<='0';--L6 S
                when x"11"=>decodSignal(6)<='1'; rgbSignal<='0';--L7 A
                when x"12"=>decodSignal(6)<='0'; rgbSignal<='0';--L7 S
                when x"13"=>decodSignal(7)<='1'; rgbSignal<='0';--L8 A
                when x"14"=>decodSignal(7)<='0'; rgbSignal<='0';--L8 S
                when x"15"=>decodSignal(8)<='1'; rgbSignal<='0';--L9 A
                when x"16"=>decodSignal(8)<='0'; rgbSignal<='0';--L9 S
                when x"17"=>decodSignal(9)<='1'; rgbSignal<='0';--L10 A
                when x"18"=>decodSignal(9)<='0'; rgbSignal<='0';--L10 S
                when x"19"=>decodSignal(10)<='1'; rgbSignal<='0';--L11 A
                when x"1A"=>decodSignal(10)<='0'; rgbSignal<='0';--L11 S
                when x"1B"=>decodSignal(11)<='1'; rgbSignal<='0';--L12 A
                when x"1C"=>decodSignal(11)<='0'; rgbSignal<='0';--L12 S
                when x"1D"=>decodSignal(12)<='1'; rgbSignal<='0';--L13 A
                when x"1E"=>decodSignal(12)<='0'; rgbSignal<='0';--L13 S
                when x"1F"=>decodSignal(13)<='1'; rgbSignal<='0';--L14 A
                when x"20"=>decodSignal(13)<='0'; rgbSignal<='0';--L14 S
                when x"21"=>decodSignal(14)<='1'; rgbSignal<='0';--L15 A
                when x"22"=>decodSignal(14)<='0'; rgbSignal<='0';--L15 S
                when x"23"=>decodSignal(15)<='1'; rgbSignal<='0';--L16 A
                when x"24"=>decodSignal(15)<='0'; rgbSignal<='0';--L16 S
                when others=>decodSignal<=x"0000"; rgbSignal<='0';
            end case;
        end if;
    end process;
    decod<=decodSignal;
    rgb<=rgbSignal;

end Behavioral;
