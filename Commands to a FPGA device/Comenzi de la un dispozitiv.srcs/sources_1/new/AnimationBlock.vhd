library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_UNSIGNED.all;
use IEEE.std_logic_arith.all;

entity AnimationBlock is
    port(clk, rst:in std_logic;
         ascii:in std_logic_vector(7 downto 0);
         decodasciiout:in std_logic_vector(15 downto 0);
         outleds:out std_logic_vector(15 downto 0));
end AnimationBlock;

architecture Behavioral of AnimationBlock is
signal en,clk_nou:std_logic;
signal num:std_logic_vector(3 downto 0):="0000";
signal decodout:std_logic_vector(15 downto 0);

component DivFrec 
 port(clk, rst: in std_logic;
      clk_nou: out std_logic);
end component;

begin
c:DivFrec port map(clk,rst,clk_nou);
--FF aprins, 00 stins buton animatie din intellij
en<=ascii(7) and ascii(6) and ascii(5) and ascii(4) and ascii(3) and ascii(2) and ascii(1) and ascii(0);
numarator4b:process(clk_nou,num,en)
            begin
                if rst='1' then
                    num<="0000";
                else
                    if rising_edge(clk_nou) then
                        if en='1' then
                            num <= num + 1;
                        end if; 
                    end if;
                end if;
            end process;

decodificator:process(num)
              begin
                  case num is 
                    when "0000"=>decodout<="0000000000000001";
                    when "0001"=>decodout<="0000000000000010";
                    when "0010"=>decodout<="0000000000000100";
                    when "0011"=>decodout<="0000000000001000";
                    when "0100"=>decodout<="0000000000010000";
                    when "0101"=>decodout<="0000000000100000";
                    when "0110"=>decodout<="0000000001000000";
                    when "0111"=>decodout<="0000000010000000";
                    when "1000"=>decodout<="0000000100000000";
                    when "1001"=>decodout<="0000001000000000";
                    when "1010"=>decodout<="0000010000000000";
                    when "1011"=>decodout<="0000100000000000";
                    when "1100"=>decodout<="0001000000000000";
                    when "1101"=>decodout<="0010000000000000";
                    when "1110"=>decodout<="0100000000000000";
                    when "1111"=>decodout<="1000000000000000";   
                    when others=>
                  end case;
              end process;
outleds<=decodout when en='1' else decodasciiout;           
end Behavioral;
