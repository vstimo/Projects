library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity RegLeds is
    port(clk, wr, rst:in std_logic;
         data:in std_logic_vector(7 downto 0);
         data_out:out std_logic_vector(7 downto 0));
end RegLeds;

architecture Behavioral of RegLeds is
begin
    process(clk,wr,data)
    begin
        if rising_edge(clk) then
            if rst = '1' then
                data_out<= (others => '0');
            else
                if wr='1' then
                    data_out<=data;
                end if;
            end if;
        end if;
    end process;

end Behavioral;
