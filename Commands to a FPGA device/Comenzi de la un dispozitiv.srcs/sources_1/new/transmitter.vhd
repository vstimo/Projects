library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.std_logic_unsigned.all;

entity transmitter is
    Port (clk, start : in  STD_LOGIC;
         data : in  STD_LOGIC_VECTOR (7 downto 0);
         tx, txRdy : out  STD_LOGIC);
end transmitter;

architecture Behavioral of transmitter is
    type state is (ready, load, send);
    signal stare : state := ready;
    constant TICK : std_logic_vector(9 downto 0) := "1101100100"; --868 = (round(100MHz /115_200))-1 

    signal clkCnt : std_logic_vector(9 downto 0) := (others => '0');
    signal bitDone : std_logic;
    signal bitIndex : natural;
    signal txBit : std_logic := '1';
    signal txData : std_logic_vector(9 downto 0);
begin
    next_txState_process : process (clk)
    begin
        if rising_edge(clk) then
            case stare is
                when ready =>
                    if start = '1' then
                        stare <= load;
                    end if;
                when load =>
                    stare <= send;
                when send =>
                    if bitDone = '1' then
                        if bitIndex = 10 then
                            stare <= ready;
                        else
                            stare <= load;
                        end if;
                    end if;
                when others=> stare <= ready;
            end case;
        end if;
    end process;

    bit_timing_process : process (clk)
    begin
        if rising_edge(clk) then
            if stare = ready then
                clkCnt <= (others => '0');
            else
                if bitDone = '1' then
                    clkCnt <= (others => '0');
                else
                    clkCnt <= clkCnt + 1;
                end if;
            end if;
        end if;
    end process;

    bitDone <= '1' when clkCnt = TICK else '0';

    bit_counting_process : process (clk)
    begin
        if rising_edge(clk) then
            if stare = ready then
                bitIndex <= 0;
            elsif stare = load then
                bitIndex <= bitIndex + 1;
            end if;
        end if;
    end process;

    tx_data_latch_process : process (clk)
    begin
        if rising_edge(clk) then
            if start = '1' then
                txData <= '1' & data & '0';
            end if;
        end if;
    end process;

    tx_bit_process : process (clk)
    begin
        if rising_edge(clk) then
            if stare = ready then
                txBit <= '1';
            elsif stare = load then
                txBit <= txData(bitIndex);
            end if;
        end if;
    end process;

    tx <= txBit;
    txRdy <= '1' when stare = ready else '0';
end Behavioral;