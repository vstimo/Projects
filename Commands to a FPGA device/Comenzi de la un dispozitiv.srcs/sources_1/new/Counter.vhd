library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity Counter is
    port (clk, rst, value: in std_logic;
         two_times: out std_logic);
end Counter;

architecture Behavioral of Counter is
    signal cnt : integer := 0;
begin
    process(clk, rst)
    begin
        if rst = '1' then
            cnt <= 0;
            two_times <= '0';
        elsif rising_edge(clk) then
            if value = '1' then
                cnt <= cnt + 1;
            end if;

            if cnt = 2 then
                two_times <= '1';
                cnt <= 0;
            else
                two_times <= '0';
            end if;
        end if;
    end process;
end Behavioral;